package com.example.brandon.trace;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;


/**
 * Created by Brandon on 9/8/2016.
 */
public class WriteFileTask extends AsyncTask<Void, Void, Void> {
    private String path;
    private String fileName;
    private HashMap<String, ByteArrayOutputStream> files;
    private ByteArrayOutputStream contents;

    public WriteFileTask(String path, String fileName, HashMap<String, ByteArrayOutputStream> files) {
        this.path = path;
        this.fileName = fileName;
        this.files = files;
        this.contents = files.get(fileName);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        FileUtils.setFileStatus(fileName, FileUtils.STATUS_SAVING);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            String name = fileName.replaceAll("\\\\", "/");
            File file = new File(path, name);

            // Create any folders needed as listed in the file
            String fullPath = file.getAbsolutePath();
            File folders = new File(fullPath.substring(0, fullPath.lastIndexOf("/")));
            folders.mkdirs();

            FileOutputStream outputStream = new FileOutputStream(file);

            outputStream.write(contents.toByteArray());
            outputStream.flush();
            outputStream.close();
            contents.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        FileUtils.setFileStatus(fileName, FileUtils.STATUS_COMPLETE);
        FileUtils.toggleFileProgress(fileName);

        files.put(fileName, null);
    }
}
