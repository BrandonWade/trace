package com.example.brandon.trace;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Asynchronously writes a received file to storage.
 */
public class WriteFileTask extends AsyncTask<Void, Void, Void> {
    private String path;
    private String fileName;
    private ByteArrayOutputStream contents;

    public WriteFileTask(String path, String fileName, ByteArrayOutputStream contents) {
        this.path = path;
        this.fileName = fileName;
        this.contents = contents;
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

            // Create any folders needed as listed in the file path
            String fullPath = file.getAbsolutePath();
            File folders = new File(fullPath.substring(0, fullPath.lastIndexOf("/")));
            folders.mkdirs();

            FileOutputStream outputStream = new FileOutputStream(file);

            outputStream.write(contents.toByteArray());
            outputStream.flush();
            outputStream.close();
            contents.close();

            FileUtils.setFileSelectable(fileName, false);
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
    }
}
