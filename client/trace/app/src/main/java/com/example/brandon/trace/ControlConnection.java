package com.example.brandon.trace;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Contains a WebSocket connection running on a separate thread used to communicate with the server.
 */
public class ControlConnection extends Thread {

    private List<String> files;
    private WebSocket conn;
    private Gson gson;
    private boolean reachable;

    private static ControlConnection controlConn;

    protected ControlConnection() {
        this.reachable = false;
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
                            reachable = true;
                            boolean enabled = reachable && !FileDownloadManager.getInstance().isDownloading() && FileUtils.getCheckedFiles().size() > 0;
                            UIUtils.toggleConfirmButton(enabled);
                            UIUtils.toggleSyncButton(reachable);
                            UIUtils.showToast(R.string.message_connected_to_server, UIUtils.SHORT_TOAST_DURATION);
                        }

                        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                            reachable = false;
                            UIUtils.toggleConfirmButton(false);
                            UIUtils.toggleSyncButton(reachable);
                            UIUtils.showToast(R.string.message_disconnected_from_server, UIUtils.SHORT_TOAST_DURATION);
                        }

                        public void onTextMessage(WebSocket websocket, String m) {
                            Message message = gson.fromJson(m, Message.class);
                            String type = message.Type;
                            switch (type) {
                                case Message.NEW:
                                    FileUtils.addFile(message.File);
                                    break;
                                case Message.DONE:
                                    boolean enabled = reachable && !FileDownloadManager.getInstance().isDownloading() && FileUtils.getCheckedFiles().size() > 0;
                                    UIUtils.toggleConfirmButton(enabled);
                                    UIUtils.toggleSyncButton(true);
                                    UIUtils.showToast(R.string.message_sync_complete, UIUtils.SHORT_TOAST_DURATION);
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

    public void setFileList(List<String> files) {
        this.files = files;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void sendFileList() {
        if (reachable) {
            FileUtils.clearFiles();

            for (String file : files) {
                String relPath = file.replace(StorageManager.storageDir, "");
                Message message = new Message(Message.NEW, relPath, "");
                conn.sendText(gson.toJson(message));
            }

            Message doneMessage = new Message(Message.DONE, "", "");
            conn.sendText(gson.toJson(doneMessage));
        }
    }
}