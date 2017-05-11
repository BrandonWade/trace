package com.example.brandon.trace;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Contains various constants and methods related to storage.
 */
public class StorageManager {
    // Permissions Code
    public static int PERMISSIONS_READ_WRITE = 1;

    // SharedPreferences file name
    public static String PREFERENCES_FILE = "PREFERENCES_FILE";

    // SharedPreferences key names
    public static String SERVER_ADDRESS_KEY = "SERVER_ADDRESS";
    public static String STORAGE_DIRECTORY_KEY = "STORAGE_DIRECTORY";
    public static String NUM_CONNECTIONS_KEY = "NUM_CONNECTIONS";

    // Application values
    public static String serverAddress = "";
    public static String storageDir = "";
    public static int numConnections = 1;

    // Singleton reference
    private static StorageManager manager;

    // SharedPreferences used for value storage and retrieval
    private static SharedPreferences preferences;

    protected StorageManager(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_FILE, MODE_PRIVATE);
    }

    public static StorageManager getManager(Context context) {
        if (manager == null) {
            manager = new StorageManager(context);
        }

        return manager;
    }

    public void retrieve() {
        serverAddress = preferences.getString(SERVER_ADDRESS_KEY, "192.168.0.7:8080");
        storageDir = preferences.getString(STORAGE_DIRECTORY_KEY, "");
        numConnections = preferences.getInt(NUM_CONNECTIONS_KEY, 1);
    }

    public void saveStorageDir(String dir) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(STORAGE_DIRECTORY_KEY, dir);
        editor.apply();
    }

    public void saveServerAddress(String address) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SERVER_ADDRESS_KEY, address);
        editor.apply();
    }

    public void saveNumConnections(int connections) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(NUM_CONNECTIONS_KEY, connections);
        editor.apply();
    }
}
