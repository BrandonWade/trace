package com.example.brandon.trace;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static com.example.brandon.trace.Storage.dir;

/**
 * Creates connections to the server to download a list of files.
 */
public class FileDownloadManager extends Thread {

    private Context context;
    private List<String> files;
    private List<FileConnection> fileConnections;

    public FileDownloadManager(Context context, List<String> files) {
        this.context = context;
        this.files = files;
        fileConnections = new ArrayList<>();
    }

    public void run() {
        int i = 0;
        Semaphore lock = new Semaphore(ControlConnection.numConnections);

        SharedPreferences preferences = context.getSharedPreferences(Storage.PREFERENCES_FILE, Context.MODE_PRIVATE);
        String address = preferences.getString(Storage.SERVER_ADDRESS_KEY, "");

        String route = address + "/file";

        while (i < files.size()) {
            try {
                lock.acquire();

                FileConnection conn = new FileConnection(lock, route, dir, files.get(i));
                conn.start();

                fileConnections.add(conn);
                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
