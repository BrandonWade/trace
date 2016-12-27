package com.example.brandon.trace;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Asynchronously scans files in a specified directory.
 */
public class ScanFilesTask extends AsyncTask<Void, Void, List<File>> {

    private Context context;
    private String dir;

    public ScanFilesTask(Context context, String dir) {
        this.context = context;
        this.dir = dir;
    }

    @Override
    protected List<File> doInBackground(Void... params) {
        return getFiles(new File(dir));
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

        SharedPreferences preferences = context.getSharedPreferences(StorageManager.PREFERENCES_FILE, Context.MODE_PRIVATE);
        String address = preferences.getString(StorageManager.SERVER_ADDRESS_KEY, "");

        ControlConnection conn = new ControlConnection(context, dir, address, paths);
        conn.start();
    }
}
