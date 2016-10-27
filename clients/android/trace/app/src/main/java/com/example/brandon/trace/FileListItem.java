package com.example.brandon.trace;

/**
 * Represents an item in a list of files.
 */
public class FileListItem {
    public String fileName;
    public String status;
    public int progress;
    public int size;
    public boolean showProgress;
    public int position;

    public FileListItem(String fileName, int size, String status) {
        this.fileName = fileName;
        this.size = size;
        this.status = status;
        this.progress = 0;
        this.showProgress = false;
    }
}
