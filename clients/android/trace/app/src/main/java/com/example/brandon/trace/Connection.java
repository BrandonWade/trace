package com.example.brandon.trace;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * Contains a WebSocket connection running on a separate thread.
 */
public class Connection extends Thread {

    private Context context;
    private String dir;
    private List<File> files;
    private WebSocketConnection conn;
    private ProgressUpdaterTask progressUpdaterTask;
    private Gson gson;

    public static String address;
    public HashMap<String, ByteArrayOutputStream> fileContents;
    public int numFiles;

    public Connection(Context context, String dir, List<File> files) {
        this.context = context;
        this.dir = dir;
        this.files = files;
        this.gson = new Gson();
        this.fileContents = new HashMap<>();
        this.progressUpdaterTask = new ProgressUpdaterTask();
    }

    public void run() {
        SharedPreferences preferences = context.getSharedPreferences(Storage.PREFERENCES_FILE, Context.MODE_PRIVATE);
        address = preferences.getString(Storage.SERVER_ADDRESS_KEY, "");

        conn = new WebSocketConnection();

        try {
            conn.connect("ws://" + address, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    super.onOpen();
                    sendFileList(conn);
                    progressUpdaterTask.run();
                }

                @Override
                public void onClose(int code, String reason) {
                    super.onClose(code, reason);
                    Toast.makeText(context, R.string.message_sync_complete, Toast.LENGTH_SHORT).show();
                    progressUpdaterTask.complete();
                }

                @Override
                public void onTextMessage(String payload) {
                    super.onTextMessage(payload);

                    Message message = gson.fromJson(payload, Message.class);
                    String type = message.Type;
                    switch (type) {
                        case Message.NEW:
                            FileUtils.addFile(message.File, message.Length);
                            FileUtils.toggleFileProgress(message.File);

                            fileContents.put(message.File, new ByteArrayOutputStream());
                            break;
                        case Message.COUNT:
                            numFiles = message.Length;
                            break;
                        case Message.PART:
                            FileUtils.setFileStatus(message.File, FileUtils.STATUS_DOWNLOADING);
                            FileUtils.updateFileProgress(message.File);

                            try {
                                fileContents.get(message.File).write(message.extractBody());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case Message.DONE:
                            FileUtils.setFileStatus(message.File, FileUtils.STATUS_DOWNLOADED);

                            WriteFileTask writeFile = new WriteFileTask(dir, message.File, fileContents);
                            writeFile.execute();
                            break;
                    }
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    private void sendFileList(WebSocketConnection conn) {
        for (File file : files) {
            String relPath = file.getAbsolutePath().replace(dir, "");
            Message message = new Message(Message.NEW, relPath, 0, "");
            conn.sendTextMessage(gson.toJson(message));
        }

        Message doneMessage = new Message(Message.DONE, "", 0, "");
        conn.sendTextMessage(gson.toJson(doneMessage));
    }
}