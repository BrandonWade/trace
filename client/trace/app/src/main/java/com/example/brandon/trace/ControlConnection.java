package com.example.brandon.trace;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contains a WebSocket connection running on a separate thread used to communicate with the server.
 */
public class ControlConnection extends Thread {

    private MainActivity mainActivity;
    private FileDownloadManager fdm;
    private List<String> files;
    private List<String> newFiles;
    private WebSocket conn;
    private Gson gson;
    private boolean reachable;

    private static ControlConnection controlConn;

    protected ControlConnection() {
        this.reachable = false;
        this.newFiles = new ArrayList<>();
        this.gson = new Gson();
    }

    public static ControlConnection getInstance() {
        if (controlConn == null) {
            controlConn = new ControlConnection();
        }

        return controlConn;
    }

    public void connect() {
        try {
            conn = new WebSocketFactory()
                    .setConnectionTimeout(5000)
                    .createSocket("ws://" + StorageManager.serverAddress + "/sync")
                    .setPingInterval(1000)
                    .addListener(new WebSocketAdapter() {

                        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                            // TODO: Send toast notification indicating connection
                            reachable = true;
                            mainActivity.toggleSyncButton(reachable);
                        }

                        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                            // TODO: Send toast notification indicating disconnection
                            reachable = false;
                            mainActivity.toggleSyncButton(reachable);
                        }

                        public void onTextMessage(WebSocket websocket, String m) {
                            Message message = gson.fromJson(m, Message.class);
                            String type = message.Type;
                            switch (type) {
                                case Message.LIST:
                                    newFiles.add(message.File);
                                    FileUtils.addFile(message.File);
                                    break;
                                case Message.DONE:
                                    fdm = new FileDownloadManager(newFiles, mainActivity);
                                    fdm.start();
                                    break;
                            }
                        }
                    })
                    .connect();
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        connect();
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setFileList(List<String> files) {
        this.files = files;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void sendFileList() {
        if (reachable) {
            if (fdm != null) {
                fdm.interrupt();
            }

            newFiles.clear();
            FileUtils.clearFiles();

            for (String file : files) {
                String relPath = file.replace(StorageManager.storageDir, "");
                Message message = new Message(Message.LIST, relPath, 0, "");
                conn.sendText(gson.toJson(message));
            }

            Message doneMessage = new Message(Message.DONE, "", 0, "");
            conn.sendText(gson.toJson(doneMessage));
        }
    }
}