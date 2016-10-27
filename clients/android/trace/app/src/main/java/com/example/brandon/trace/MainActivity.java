package com.example.brandon.trace;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences(Storage.PREFERENCES_FILE, MODE_PRIVATE);
        dir = preferences.getString(Storage.STORAGE_DIRECTORY_KEY, "");
        Connection.address = preferences.getString(Storage.SERVER_ADDRESS_KEY, "");

        // Used to retrieve an external dir
//        File[] storageDirs = getApplicationContext().getExternalMediaDirs();
//        File dir = storageDirs[1];
//        File f = new File(dir, fileName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem syncButton = menu.findItem(R.id.action_sync);
        ServerStatusCheck serverStatusCheck = new ServerStatusCheck(this, syncButton);
        serverStatusCheck.run();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync:
                isStoragePermissionGranted();
                return true;
            case R.id.action_connection:
                showConnectionActivity();
                return true;
            case R.id.action_settings:
                showSettingsActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                syncFiles();
                return true;
            } else {
                String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };
                ActivityCompat.requestPermissions(this, permissions, Storage.PERMISSIONS_READ_WRITE);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Storage.PERMISSIONS_READ_WRITE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            syncFiles();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.desc_need_permissions)
                .setCancelable(false)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void syncFiles() {
        Toast.makeText(getApplicationContext(), R.string.message_syncing, Toast.LENGTH_SHORT).show();
        FileUtils.fileList.clear();
        FileUtils.fileListAdapter.notifyDataSetChanged();

        ScanFilesTask scanFiles = new ScanFilesTask(getApplicationContext(), dir);
        scanFiles.execute();
    }

    public void showConnectionActivity() {
        Intent intent = new Intent(this, ConnectionActivity.class);
        startActivity(intent);
    }

    public void showSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
