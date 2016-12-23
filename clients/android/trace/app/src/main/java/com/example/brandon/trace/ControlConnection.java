package com.example.brandon.trace;

import android.content.Context;
import android.util.Log;

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

    private Context context;
    private String dir;
    private String address;
    private List<String> files;
    private List<String> newFiles;
    private WebSocket conn;
    private Gson gson;

    public static int numConnections = 1;
    public int numFiles;

    public ControlConnection(Context context, String dir, String address, List<String> files) {
        this.context = context;
        this.dir = dir;
        this.address = address;
        this.files = files;
        this.newFiles = new ArrayList<>();
        this.gson = new Gson();
    }

    public void run() {
        try {
            conn = new WebSocketFactory()
                    .setConnectionTimeout(5000)
                    .createSocket("ws://" + address)
                    .addListener(new WebSocketAdapter() {

                        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                            // TODO: Send toast notification indicating connection
                            sendFileList(conn);
                        }

                        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                            // TODO: Send toast notification indicating disconnection
                        }

                        public void onTextMessage(WebSocket websocket, String m) {
                            Message message = gson.fromJson(m, Message.class);
                            String type = message.Type;
                            switch (type) {
                                case Message.COUNT:
                                    numFiles = message.Length;
                                    break;
                                case Message.LIST:
                                    newFiles.add(message.File);
                                    break;
                                case Message.LIST_COMPLETE:
                                    FileDownloadManager fdm = new FileDownloadManager(context, newFiles, dir);
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

    private void sendFileList(WebSocket conn) {
        for (String file : files) {
            String relPath = file.replace(dir, "");
            Message message = new Message(Message.LIST, relPath, 0, "");
            conn.sendText(gson.toJson(message));
        }

        Message doneMessage = new Message(Message.DONE, "", 0, "");
        conn.sendText(gson.toJson(doneMessage));
    }
}