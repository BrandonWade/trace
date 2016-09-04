package com.example.brandon.trace;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        Connection conn = new Connection(getApplicationContext());
        conn.execute();
    }

    private class Connection extends AsyncTask<Void, Void, Void> {
        Context context;
        WebSocketConnection conn;
        Gson gson;
        String file;

        public Connection(Context context) {
            this.context = context;
            this.gson = new Gson();
            this.file = "";
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
                        if (message.Type.equals("part")) {
                            int bound = Math.min(message.Body.length(), message.Length);
                            String contents = message.Body.substring(0, bound);
                            file += contents;
                        } else if (message.Type.equals("done")) {
                            Log.i("Trace", file);
                            Toast.makeText(context, "DONE", Toast.LENGTH_SHORT).show();
                            textView.setText(file);
                        }
                    }
                });
            } catch (WebSocketException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}