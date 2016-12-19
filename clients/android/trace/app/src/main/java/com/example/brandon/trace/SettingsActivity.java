package com.example.brandon.trace;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private int numConnections = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = getSharedPreferences(Storage.PREFERENCES_FILE, MODE_PRIVATE);
        String serverAddress = preferences.getString(Storage.STORAGE_DIRECTORY_KEY, "");

        EditText addressField = (EditText) findViewById(R.id.storage_directory_field);
        addressField.setText(serverAddress);

        final TextView numConnectionsLabel = (TextView)findViewById(R.id.connections_label);
        final SeekBar maxConnectionsBar = (SeekBar)findViewById(R.id.max_connections_slider);
        numConnectionsLabel.setText(String.valueOf(numConnections));

        maxConnectionsBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numConnections = (progress + 1);
                numConnectionsLabel.setText(String.valueOf(numConnections));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void setStorageDirectory(View view) {
        EditText directoryField = (EditText) findViewById(R.id.storage_directory_field);
        String directory = directoryField.getText().toString();

        SharedPreferences preferences = getSharedPreferences(Storage.PREFERENCES_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Storage.STORAGE_DIRECTORY_KEY, directory);
        editor.apply();
    }
}
