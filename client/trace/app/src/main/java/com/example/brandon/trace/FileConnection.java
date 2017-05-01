package com.example.brandon.trace;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketState;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * Contains a WebSocket connection running on a separate thread used to receive files from the server.
 */
public class FileConnection extends Thread {

    private Semaphore lock;
    private String file;
    private WebSocket conn;
    private Gson gson;
    private ByteArrayOutputStream fileContents;

    public FileConnection(Semaphore lock, String file) {
        this.lock = lock;
        this.file = file;
        this.gson = new Gson();
        this.fileContents = new ByteArrayOutputStream();
    }

    public void run() {
        try {
            conn = new WebSocketFactory()
                    .setConnectionTimeout(5000)
                    .createSocket("ws://" + StorageManager.serverAddress + "/file")
                    .addListener(new WebSocketAdapter() {

                        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                            sendFile(websocket);
                        }

                        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                            lock.release();
                        }

                        public void onTextMessage(WebSocket websocket, String m) {
                            Message message = gson.fromJson(m, Message.class);
                            String type = message.Type;
                            switch (type) {
                                case Message.NEW:
                                    FileUtils.setFileSize(file, (double)message.Length);
                                    FileUtils.toggleFileProgress(file);
                                    break;
                                case Message.DONE:
                                    FileUtils.setFileStatus(file, FileUtils.STATUS_DOWNLOADED);
                                    conn.disconnect(); // TODO: Pass into WFT below, trigger there instead

                                    WriteFileTask writeFile = new WriteFileTask(StorageManager.storageDir, file, fileContents, conn);
                                    writeFile.execute();
                                    break;
                            }
                        }

                        public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
                            FileUtils.setFileStatus(file, FileUtils.STATUS_DOWNLOADING);
                            FileUtils.updateFileProgress(file);

                            try {
                                fileContents.write(binary);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
                            cause.printStackTrace();
                        }
                    })
                    .connect();
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(WebSocket conn) {
        String relPath = file.replace(StorageManager.storageDir, "");
        Message message = new Message(Message.NEW, relPath, 0, "");
        conn.sendText(gson.toJson(message));
    }

    public void disconnect() {
        conn.disconnect();
    }

    public boolean isDisconnected() {
        return (conn != null && conn.getState() == WebSocketState.CLOSED);
    }
}