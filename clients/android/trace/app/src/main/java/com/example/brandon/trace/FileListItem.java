package com.example.brandon.trace;

/**
 * Created by Brandon on 9/27/2016.
 */

public class FileListItem {
    public String fileName;
    public String status;
    public int progress;
    public int size;
    public boolean showProgress;

    public FileListItem(String fileName, int size, String status) {
        this.fileName = fileName;
        this.size = size;
        this.status = status;
        this.progress = 0;
        this.showProgress = false;
    }
}
