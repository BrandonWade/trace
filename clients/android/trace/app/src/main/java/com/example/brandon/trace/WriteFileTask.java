package com.example.brandon.trace;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by Brandon on 9/8/2016.
 */
public class WriteFileTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private String path;
    private String fileName;
    private ByteArrayOutputStream contents;

    public WriteFileTask(Context context, String path, String fileName, ByteArrayOutputStream contents) {
        this.context = context;
        this.path = path;
        this.fileName = fileName;
        this.contents = contents;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context, "SAVED - " + path + fileName, Toast.LENGTH_SHORT).show();
    }
}
