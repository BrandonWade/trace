package com.example.brandon.trace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText addressField = (EditText) findViewById(R.id.storage_directory_field);
        addressField.setText(StorageManager.storageDir);
    }

    public void setStorageDirectory(View view) {
        EditText directoryField = (EditText) findViewById(R.id.storage_directory_field);
        String directory = directoryField.getText().toString();

        StorageManager manager = StorageManager.getManager(getApplicationContext());
        manager.saveStorageDir(directory);
    }
}
