package com.example.brandon.trace;

import android.content.Context;
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
 * Created by Brandon on 9/4/2016.
 */
public class Connection extends Thread {
    private Context context;
    private String dir;
    private List<File> files;
    private WebSocketConnection conn;
    private Gson gson;
    private HashMap<String, ByteArrayOutputStream> fileContents;
    private int numFiles;

    public Connection(Context context, String dir, List<File> files) {
        this.context = context;
        this.dir = dir;
        this.files = files;
        this.gson = new Gson();
        this.fileContents = new HashMap<>();
    }

    public void run() {
        conn = new WebSocketConnection();

        try {
            conn.connect("ws://192.168.0.11:8080", new WebSocketHandler() {
                @Override
                public void onOpen() {
                    super.onOpen();
                    sendFileList(conn);
                    Toast.makeText(context, "OPEN", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onClose(int code, String reason) {
                    super.onClose(code, reason);
                    Toast.makeText(context, "CLOSE - " + reason, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onTextMessage(String payload) {
                    super.onTextMessage(payload);

                    Message message = gson.fromJson(payload, Message.class);
                    String type = message.Type;
                    switch (type) {
                        case Message.NEW:
                            FileUtils.addFile(message.File);

                            fileContents.put(message.File, new ByteArrayOutputStream());
                            break;
                        case Message.COUNT:
                            numFiles = message.Length;
                            break;
                        case Message.PART:
                            FileUtils.setFileStatus(message.File, FileUtils.STATUS_DOWNLOADING);

                            try {
                                fileContents.get(message.File).write(message.extractBody());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case Message.DONE:
                            FileUtils.setFileStatus(message.File, FileUtils.STATUS_DOWNLOADED);

                            WriteFileTask writeFile = new WriteFileTask(context, dir, message.File, fileContents.get(message.File));
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