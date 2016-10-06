package com.example.brandon.trace;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // TODO: Use a real dir
    public String dir = "/storage/6463-6331/Android/media/com.example.brandon.trace";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        File[] storageDirs = context.getExternalMediaDirs();
//        File dir = storageDirs[1];
//        File f = new File(dir, fileName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync:
                syncFiles();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void syncFiles() {
        Toast.makeText(getApplicationContext(), R.string.message_syncing, Toast.LENGTH_SHORT).show();
        FileUtils.fileList.clear();
        FileUtils.fileListAdapter.notifyDataSetChanged();

        ScanFilesTask scanFiles = new ScanFilesTask(getApplicationContext(), dir);
        scanFiles.execute();
    }
}
