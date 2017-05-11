package com.example.brandon.trace;

import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Creates connections to the server to download a list of files.
 */
public class FileDownloadManager extends Thread {

    private boolean downloading;

    private static FileDownloadManager fdm;

    protected FileDownloadManager() {
        this.downloading = false;
    }

    public static FileDownloadManager getInstance() {
        if (fdm == null) {
            fdm = new FileDownloadManager();
        }

        return fdm;
    }

    public void download() {
        List<FileListItem> files = FileUtils.getCheckedFiles();
        Semaphore lock = new Semaphore(StorageManager.numConnections);

        UIUtils.toggleConfirmButton(false);
        downloading = true;

        for (FileListItem file : FileUtils.fileList) {
            FileUtils.setFileEnabled(file.fileName, false);
        }

        for (int i = 0; i < files.size(); i++) {
            try {
                lock.acquire();

                FileConnection conn = new FileConnection(lock, files.get(i).fileName);
                conn.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            lock.acquire(StorageManager.numConnections);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (FileListItem file : FileUtils.fileList) {
            FileUtils.setFileEnabled(file.fileName, file.selectable);
        }

        downloading = false;
        boolean enabled = ControlConnection.getInstance().isReachable() && !downloading && FileUtils.getCheckedFiles().size() > 0;
        UIUtils.toggleConfirmButton(enabled);
        lock.release(StorageManager.numConnections);
    }

    public void run() {
        download();
    }

    public boolean isDownloading() {
        return downloading;
    }
}
