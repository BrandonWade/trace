package com.example.brandon.trace;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

/**
 * Contains utility functions for updating the file display.
 */
public class FileUtils {

    public static final String STATUS_WAITING = "Waiting";
    public static final String STATUS_DOWNLOADING = "Downloading";
    public static final String STATUS_DOWNLOADED = "Downloaded";
    public static final String STATUS_SAVING = "Saving";
    public static final String STATUS_COMPLETE = "Complete";

    public static ArrayList<FileListItem> fileList;
    public static FileListItemAdapter fileListAdapter;

    private static Handler redrawHandler = new Handler(Looper.getMainLooper());

    private static FileListItem findByName(String fileName) {
        for (FileListItem file: fileList) {
            if (file.fileName.equals(fileName)) {
                return file;
            }
        }

        return null;
    }

    public static void addFile(final String fileName) {
        Runnable updateUI = new Runnable() {
            @Override public void run() {
                FileListItem file = new FileListItem(fileName, STATUS_WAITING);
                fileList.add(file);

                fileListAdapter.notifyDataSetChanged();
            }
        };

        redrawHandler.post(updateUI);
    }

    public static void setFileStatus(final String fileName, final String status) {
        Runnable updateUI = new Runnable() {
            @Override public void run() {
                FileListItem file = findByName(fileName);

                if (file != null) {
                    file.status = status;
                }

                fileListAdapter.notifyDataSetChanged();
            }
        };

        redrawHandler.post(updateUI);
    }

    public static void setFileSize(final String fileName, final double size) {
        Runnable updateUI = new Runnable() {
            @Override public void run() {
                FileListItem file = findByName(fileName);

                if (file != null) {
                    file.size = size;
                }

                fileListAdapter.notifyDataSetChanged();
            }
        };

        redrawHandler.post(updateUI);
    }

    public static void updateFileProgress(final String fileName) {
        Runnable updateUI = new Runnable() {
            @Override public void run() {
                FileListItem file = findByName(fileName);

                if (file != null) {
                    file.progress++;
                }

                fileListAdapter.notifyDataSetChanged();
            }
        };

        redrawHandler.post(updateUI);
    }

    public static void toggleFileProgress(final String fileName) {
        Runnable updateUI = new Runnable() {
            @Override public void run() {
                FileListItem file = findByName(fileName);

                if (file != null) {
                    file.showProgress = !file.showProgress;
                }

                fileListAdapter.notifyDataSetChanged();
            }
        };

        redrawHandler.post(updateUI);
    }

    public static void clearFiles() {
        Runnable updateUI = new Runnable() {
            @Override public void run() {
                fileList.clear();

                fileListAdapter.notifyDataSetChanged();
            }
        };

        redrawHandler.post(updateUI);
    }
}