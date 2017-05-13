package com.example.brandon.trace;

import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Asynchronously scans files in a specified directory.
 */
public class ScanFilesTask extends AsyncTask<Void, Void, List<File>> {

    @Override
    protected List<File> doInBackground(Void... params) {
        if (StorageManager.storageDir.equals("")) {
            return null;
        }

        return getFiles(new File(StorageManager.storageDir));
    }

    private List<File> getFiles(File parentDir) {
        List<File> files = new ArrayList<>();
        File[] parentDirFiles = parentDir.listFiles();

        for (File file : parentDirFiles) {
            if (file.isDirectory()) {
                files.addAll(getFiles(file));
            } else {
                files.add(file);
            }
        }

        return files;
    }

    @Override
    protected void onPostExecute(List<File> files) {
        if (files == null) {
            UIUtils.toggleConfirmButton(UIUtils.canConfirmDownload());
            UIUtils.toggleSyncButton(true);
            UIUtils.showToast(R.string.message_no_directory_set, UIUtils.LONG_TOAST_DURATION);
            return;
        }

        List<String> paths = new ArrayList<>();

        for (File file : files) {
            paths.add(file.getAbsolutePath());
        }

        ControlConnection controlConn = ControlConnection.getInstance();
        controlConn.setFileList(paths);
        controlConn.sendFileList();
    }
}
