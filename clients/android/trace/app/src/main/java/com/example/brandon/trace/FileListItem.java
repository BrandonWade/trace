package com.example.brandon.trace;

/**
 * Represents an item in a list of files.
 */
public class FileListItem {
    public String fileName;
    public String status;
    public double size;
    public double progress;
    public boolean showProgress;

    public FileListItem(String fileName, String status) {
        this.fileName = fileName;
        this.status = status;
        this.size = -1;
        this.progress = 0;
        this.showProgress = false;
    }
}
