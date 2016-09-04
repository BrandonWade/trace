package com.example.brandon.trace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MainActivity extends AppCompatActivity {

    WebSocketConnection conn;
    String file = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conn = new WebSocketConnection();

        try {
            conn.connect("ws://192.168.0.10:8080", new WebSocketHandler() {
                @Override
                public void onOpen() {
                    super.onOpen();
                    Toast.makeText(getApplicationContext(), "OPEN", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onClose(int code, String reason) {
                    super.onClose(code, reason);
                    Toast.makeText(getApplicationContext(), "CLOSE - " + reason, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onTextMessage(String payload) {
                    super.onTextMessage(payload);

                    Gson gson = new Gson();
                    Message message = gson.fromJson(payload, Message.class);

                    if (message.Type.equals("part")) {
                        int bound = Math.min(message.Body.length(), message.Length);
                        String contents = message.Body.substring(0, bound);
                        file += contents;
                    } else if (message.Type.equals("done")) {
                        TextView tv = (TextView) findViewById(R.id.textView);
                        tv.setMovementMethod(new ScrollingMovementMethod());
                        tv.setText(file);
                    }
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
//
//        Button btn = (Button)findViewById(R.id.button);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                conn.sendTextMessage("Hello world!");
//            }
//        });
    }
}
