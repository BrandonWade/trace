package com.example.brandon.trace;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
<<<<<<< HEAD
import android.view.Menu;
import android.view.MenuInflater;
=======
>>>>>>> d09d279ec3404556f00b69d7a4d0d6bc65c168f0

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
}
