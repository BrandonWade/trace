package com.example.brandon.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Creates connections to the server to download a list of files.
 */
public class FileDownloadManager extends Thread {

    private List<String> files;
    private List<FileConnection> fileConnections;

    public FileDownloadManager(List<String> files) {
        this.files = files;
        this.fileConnections = new ArrayList<>();
    }

    public void run() {
        Semaphore lock = new Semaphore(StorageManager.numConnections);

        for (int i = 0; i < files.size(); i++) {
            if (this.isInterrupted()) {
                cancel();
                return;
            }

            try {
                lock.acquire();

                FileConnection conn = new FileConnection(lock, files.get(i));
                conn.start();

                fileConnections.add(conn);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel() {
        for (FileConnection conn: fileConnections) {
            if (!conn.isInterrupted()) {
                conn.disconnect();
            }
        }
    }
}