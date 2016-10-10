package com.example.brandon.trace;

import java.util.ArrayList;

/**
 * Created by Brandon on 9/28/2016.
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
            if (file.fileName.equals(fileName))    {
                return file;
            }
        }

        return null;
    }

    public static void addFile(String fileName, int fileSize) {
        FileListItem file = new FileListItem(fileName, fileSize, STATUS_NONE);
        fileList.add(file);
        fileListAdapter.notifyDataSetChanged();
    }

    public static void setFileStatus(String fileName, String status) {
        FileListItem file = findByName(fileName);
        file.status = status;
        fileListAdapter.notifyDataSetChanged();
    }

    public static void updateFileProgress(String fileName) {
        FileListItem file = findByName(fileName);
        file.progress++;
        fileListAdapter.notifyDataSetChanged();
    }

    public static void toggleFileProgress(String fileName) {
        FileListItem file = findByName(fileName);
        file.showProgress = !file.showProgress;
        fileListAdapter.notifyDataSetChanged();
    }
}
