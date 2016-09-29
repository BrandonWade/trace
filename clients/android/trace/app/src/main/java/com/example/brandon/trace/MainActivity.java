package com.example.brandon.trace;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends ListActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FileUtils.fileList = new ArrayList<>();
        FileUtils.fileListAdapter = new FileListItemAdapter(this, R.layout.file_list_row, FileUtils.fileList);
        ListView list = (ListView)findViewById(android.R.id.list);
        list.setAdapter(FileUtils.fileListAdapter);

        // TODO: Use a real dir
        String dir = "/storage/6463-6331/Android/media/com.example.brandon.trace";

//        File[] storageDirs = context.getExternalMediaDirs();
//        File dir = storageDirs[1];
//        File f = new File(dir, fileName);

        ScanFilesTask scanFiles = new ScanFilesTask(getApplicationContext(), dir);
        scanFiles.execute();
    }
}
