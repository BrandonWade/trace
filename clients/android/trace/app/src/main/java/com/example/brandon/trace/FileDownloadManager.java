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
        int i = 0;
        Semaphore lock = new Semaphore(StorageManager.numConnections);

        while (i < files.size()) {
            try {
                lock.acquire();

                FileConnection conn = new FileConnection(lock, files.get(i));
                conn.start();

                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
