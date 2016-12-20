package com.example.brandon.trace;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains a WebSocket connection running on a separate thread.
 */
public class Connection extends Thread {

    private Context context;
    private String dir;
    private List<File> files;
    private WebSocket conn;
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

        try {
            conn = new WebSocketFactory()
                    .setConnectionTimeout(5000)
                    .createSocket("ws://" + address)
                    .addListener(new WebSocketAdapter() {
                        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                            sendFileList(conn);
                            progressUpdaterTask.run();
                        }

                        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                            progressUpdaterTask.complete();
                        }

                        public void onTextMessage(WebSocket websocket, String m) {
                            Message message = gson.fromJson(m, Message.class);
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
                    })
                    .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                    .connectAsynchronously();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFileList(WebSocket conn) {
        for (File file : files) {
            String relPath = file.getAbsolutePath().replace(dir, "");
            Message message = new Message(Message.NEW, relPath, 0, "");
            conn.sendText(gson.toJson(message));
        }

        Message doneMessage = new Message(Message.DONE, "", 0, "");
        conn.sendText(gson.toJson(doneMessage));
    }
}