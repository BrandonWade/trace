package com.example.brandon.trace;

import java.util.ArrayList;

/**
 * Contains utility functions for updating the file display.
 */
public class FileUtils {

    public static final String STATUS_NONE = "";
    public static final String STATUS_DOWNLOADING = "Downloading";
    public static final String STATUS_DOWNLOADED = "Downloaded";
    public static final String STATUS_SAVING = "Saving";
    public static final String STATUS_COMPLETE = "Complete";

    public static ArrayList<FileListItem> fileList;
    public static FileListItemAdapter fileListAdapter;

    private static FileListItem findByName(String fileName) {
        for (FileListItem file: fileList) {
            if (file.fileName.equals(fileName)) {
                return file;
            }
        }

        return null;
    }

    public static void addFile(String fileName, int fileSize) {
        FileListItem file = new FileListItem(fileName, fileSize, STATUS_NONE);
        fileList.add(file);
    }

    public static void setFileStatus(String fileName, String status) {
        FileListItem file = findByName(fileName);

        if (file != null) {
            file.status = status;
        }
    }

    public static void updateFileProgress(String fileName) {
        FileListItem file = findByName(fileName);

        if (file != null) {
            file.progress++;
        }
    }

    public static void toggleFileProgress(String fileName) {
        FileListItem file = findByName(fileName);

        if (file != null) {
            file.showProgress = !file.showProgress;
        }
    }
}