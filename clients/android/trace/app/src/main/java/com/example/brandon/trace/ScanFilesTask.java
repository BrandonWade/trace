package com.example.brandon.trace;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 9/9/2016.
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
        ArrayList<File> files = new ArrayList<>();
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
        Connection conn = new Connection(context, dir, files);
        conn.run();
    }
}
