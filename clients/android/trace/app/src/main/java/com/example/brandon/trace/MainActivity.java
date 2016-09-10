package com.example.brandon.trace;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Use a real dir
        String dir = "/storage/6463-6331/Android/media/com.example.brandon.trace";

//        File[] storageDirs = context.getExternalMediaDirs();
//        File dir = storageDirs[1];
//        File f = new File(dir, fileName);

        ScanFilesTask scanFiles = new ScanFilesTask(getApplicationContext(), dir);
        scanFiles.execute();
    }
}
