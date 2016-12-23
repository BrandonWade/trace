package com.example.brandon.trace;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Creates connections to the server to download a list of files.
 */
public class FileDownloadManager extends Thread {

    private Context context;
    private List<String> files;
    private String dir;

    public FileDownloadManager(Context context, List<String> files, String dir) {
        this.context = context;
        this.files = files;
        this.dir = dir;
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

                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
