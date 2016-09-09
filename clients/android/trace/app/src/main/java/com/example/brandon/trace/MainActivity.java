package com.example.brandon.trace;


import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Use a real dir
        String dir = "/storage/6463-6331/Android/media/com.example.brandon.trace";

        ScanFilesTask scanFiles = new ScanFilesTask(getApplicationContext(), dir);
        scanFiles.execute();

        File sdcard = Environment.getExternalStorageDirectory();
        Log.i("", sdcard.getAbsolutePath());
    }
}

// TODO: Send list of files on WS connection
// - Consider converting Connection AsyncTask to Thread
// - Consider converting WriteFileTask to use Futures & Executor instead of AsyncTask

// Note: Used to get dir
/*
            File[] storageDirs = context.getExternalMediaDirs();
            File dir = storageDirs[1];
            File f = new File(dir, fileName);
*/