package com.example.brandon.trace;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends ListActivity {

    public static ArrayList<FileListItem> fileList = new ArrayList<>();
    public static FileListItemAdapter fileListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileListAdapter = new FileListItemAdapter(this, R.layout.file_list_row, fileList);
        ListView list = (ListView)findViewById(android.R.id.list);
        list.setAdapter(fileListAdapter);

        // TODO: Use a real dir
        String dir = "/storage/6463-6331/Android/media/com.example.brandon.trace";

//        File[] storageDirs = context.getExternalMediaDirs();
//        File dir = storageDirs[1];
//        File f = new File(dir, fileName);

        ScanFilesTask scanFiles = new ScanFilesTask(getApplicationContext(), dir);
        scanFiles.execute();
    }
}
