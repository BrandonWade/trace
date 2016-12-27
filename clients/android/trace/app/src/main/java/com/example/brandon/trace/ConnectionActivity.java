package com.example.brandon.trace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class ConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText addressField = (EditText) findViewById(R.id.server_address_field);
        addressField.setText(StorageManager.serverAddress);

        final TextView numConnectionsLabel = (TextView)findViewById(R.id.connections_label);
        final SeekBar maxConnectionsBar = (SeekBar)findViewById(R.id.max_connections_slider);
        numConnectionsLabel.setText(String.valueOf(StorageManager.numConnections));
        maxConnectionsBar.setProgress(StorageManager.numConnections - 1);

        maxConnectionsBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                StorageManager.numConnections = (progress + 1);
                numConnectionsLabel.setText(String.valueOf(StorageManager.numConnections));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                StorageManager manager = StorageManager.getManager(getApplicationContext());
                manager.saveNumConnections(StorageManager.numConnections);
            }
        });
    }

    public void setServerAddress(View view) {
        EditText addressField = (EditText) findViewById(R.id.server_address_field);
        String address = addressField.getText().toString();

        StorageManager manager = StorageManager.getManager(getApplicationContext());
        manager.saveServerAddress(address);
    }
}
