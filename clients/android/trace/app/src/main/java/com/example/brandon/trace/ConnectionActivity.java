package com.example.brandon.trace;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = getSharedPreferences(Storage.PREFERENCES_FILE, MODE_PRIVATE);
        String serverAddress = preferences.getString(Storage.SERVER_ADDRESS_KEY, "");

        EditText addressField = (EditText) findViewById(R.id.server_address_field);
        addressField.setText(serverAddress);
    }

    public void setServerAddress(View view) {
        EditText addressField = (EditText) findViewById(R.id.server_address_field);
        String address = addressField.getText().toString();

        SharedPreferences preferences = getSharedPreferences(Storage.PREFERENCES_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Storage.SERVER_ADDRESS_KEY, address);
        editor.apply();
    }
}
