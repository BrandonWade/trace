package com.example.brandon.trace;

import android.util.Log;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * Contains a WebSocket connection running on a separate thread uses to receive files from the server.
 */
public class FileConnection extends Thread {

    private Semaphore lock;
    private String address;
    private String dir;
    private String file;
    private WebSocket conn;
    private Gson gson;
    private ByteArrayOutputStream fileContents;

    public FileConnection(Semaphore lock, String address, String dir, String file) {
        this.lock = lock;
        this.address = address;
        this.dir = dir;
        this.file = file;
        this.gson = new Gson();
        this.fileContents = new ByteArrayOutputStream();
    }

    public void run() {
        try {
            conn = new WebSocketFactory()
                    .setConnectionTimeout(5000)
                    .createSocket("ws://" + address)
                    .addListener(new WebSocketAdapter() {

                        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                            sendFile(conn);
                        }

                        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                            lock.release();
                        }

                        public void onTextMessage(WebSocket websocket, String m) {
                            Message message = gson.fromJson(m, Message.class);
                            String type = message.Type;
                            switch (type) {
                                case Message.NEW:
                                    FileUtils.addFile(message.File, message.Length);
                                    FileUtils.toggleFileProgress(message.File);
                                    break;
                                case Message.PART:
                                    FileUtils.setFileStatus(message.File, FileUtils.STATUS_DOWNLOADING);
                                    FileUtils.updateFileProgress(message.File);

                                    try {
                                        fileContents.write(message.extractBody());
                                    } catch (IOException e) {
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
                    .connect();
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(WebSocket conn) {
        String relPath = file.replace(dir, "");
        Message message = new Message(Message.NEW, relPath, 0, "");
        String x = gson.toJson(message);
        conn.sendText(x);
    }
}