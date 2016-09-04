package com.example.brandon.trace;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * Created by Brandon on 9/4/2016.
 */
public class Connection extends AsyncTask<Void, Void, Void> {
    Context context;
    WebSocketConnection conn;
    Gson gson;
    StringBuilder file;

    public Connection(Context context) {
        this.context = context;
        this.gson = new Gson();
        this.file = new StringBuilder();
    }

    @Override
    protected Void doInBackground(Void... params) {
        conn = new WebSocketConnection();

        try {
            conn.connect("ws://192.168.0.10:8080", new WebSocketHandler() {
                @Override
                public void onOpen() {
                    super.onOpen();
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
                        case "part":
                            int bound = Math.min(message.Body.length(), message.Length);
                            String contents = message.Body.substring(0, bound);
                            file.append(contents);
                            break;
                        case "done":
                            Log.i("Trace", file.toString());
                            Toast.makeText(context, "DONE", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }

        return null;
    }
}