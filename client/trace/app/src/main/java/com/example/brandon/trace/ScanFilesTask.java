package com.example.brandon.trace;

import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Asynchronously scans files in a specified directory.
 */
public class ScanFilesTask extends AsyncTask<Void, Void, List<File>> {

    private MainActivity mainActivity;

    public ScanFilesTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected List<File> doInBackground(Void... params) {
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
        List<String> paths = new ArrayList<>();
        for (File file : files) {
            paths.add(file.getAbsolutePath());
        }

        ControlConnection controlConn = ControlConnection.getInstance();
        controlConn.setMainActivity(mainActivity);
        controlConn.setFileList(paths);
        controlConn.sendFileList();
    }
}
