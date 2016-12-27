package com.example.brandon.trace;

import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Creates connections to the server to download a list of files.
 */
public class FileDownloadManager extends Thread {

    private List<String> files;

    public FileDownloadManager(List<String> files) {
        this.files = files;
    }

    public void run() {
        Semaphore lock = new Semaphore(StorageManager.numConnections);

        for (String file : files) {
            try {
                lock.acquire();

                FileConnection conn = new FileConnection(lock, file);
                conn.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
