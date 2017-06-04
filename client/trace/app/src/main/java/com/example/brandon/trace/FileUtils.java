package com.example.brandon.trace;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains utility functions for updating the file display.
 */
public class FileUtils {

    public static final String STATUS_WAITING = "Waiting";
    public static final String STATUS_DOWNLOADING = "Downloading";
    public static final String STATUS_DOWNLOADED = "Downloaded";
    public static final String STATUS_SAVING = "Saving";
    public static final String STATUS_COMPLETE = "Complete";

    public static List<FileListItem> fileList;
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

    public static List<FileListItem> getCheckedFiles() {
        List<FileListItem> selectedFiles = new ArrayList<>();

        for (FileListItem file: fileList) {
            if (file.checked) {
                selectedFiles.add(file);
            }
        }

        return selectedFiles;
    }

    public static void addFile(final String fileName) {
        Runnable updateUI = new Runnable() {
            @Override public void run() {
                String displayName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                FileListItem file = new FileListItem(fileName, displayName, STATUS_WAITING);
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
                    fileListAdapter.notifyDataSetChanged();
                }

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
                    fileListAdapter.notifyDataSetChanged();
                }
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
                    fileListAdapter.notifyDataSetChanged();
                }
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
                    fileListAdapter.notifyDataSetChanged();
                }
            }
        };

        redrawHandler.post(updateUI);
    }

    public static void setFileChecked(final String fileName, final boolean checked) {
        Runnable updateUI = new Runnable() {
            @Override public void run() {
                FileListItem file = findByName(fileName);

                if (file != null) {
                    file.checked = checked;
                    fileListAdapter.notifyDataSetChanged();
                }
            }
        };

        redrawHandler.post(updateUI);
    }

    public static void setFileEnabled(final String fileName, final boolean enabled) {
        Runnable updateUI = new Runnable() {
            @Override public void run() {
                FileListItem file = findByName(fileName);

                if (file != null) {
                    file.enabled = enabled;
                    fileListAdapter.notifyDataSetChanged();
                }
            }
        };

        redrawHandler.post(updateUI);
    }

    public static void setFileSelectable(final String fileName, final boolean selectable) {
        Runnable updateUI = new Runnable() {
            @Override public void run() {
                FileListItem file = findByName(fileName);

                if (file != null) {
                    file.selectable = selectable;
                    fileListAdapter.notifyDataSetChanged();
                }
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
