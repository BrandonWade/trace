package com.example.brandon.trace;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

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
    ByteArrayOutputStream file;

    public Connection(Context context) {
        this.context = context;
        this.gson = new Gson();
        this.file = new ByteArrayOutputStream();
    }

    @Override
    protected Void doInBackground(Void... params) {
        conn = new WebSocketConnection();

        try {
            conn.connect("ws://192.168.0.11:8080", new WebSocketHandler() {
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
                        case Message.TYPE_PART:
                            try {
                                file.write(message.extractBody());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        case Message.TYPE_DONE:
                            saveFile(message.File);
                            break;
                    }
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Write a file to disk
    public void saveFile(String fileName) {
        try {
            File[] storageDirs = context.getExternalMediaDirs();
            File dir = storageDirs[1];
            File f = new File(dir, fileName);
            FileOutputStream outputStream = new FileOutputStream(f);

            outputStream.write(file.toByteArray());
            outputStream.flush();
            outputStream.close();
            Toast.makeText(context, "SAVED - " + f.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}