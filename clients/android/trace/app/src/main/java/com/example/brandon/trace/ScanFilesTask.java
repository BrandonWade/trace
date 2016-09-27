package com.example.brandon.trace;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 9/9/2016.
 */
public class ScanFilesTask extends AsyncTask<Void, Void, List<File>> {

    private Context context;
    private String dir;
    private TextView messages;

    public ScanFilesTask(Context context, String dir, TextView messages) {
        this.context = context;
        this.dir = dir;
        this.messages = messages;
    }

    @Override
    protected List<File> doInBackground(Void... params) {
        return getFiles(new File(dir));
    }

    @Override
    protected void onPostExecute(List<File> files) {
        Connection conn = new Connection(context, dir, files, messages);
        conn.run();
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
}
