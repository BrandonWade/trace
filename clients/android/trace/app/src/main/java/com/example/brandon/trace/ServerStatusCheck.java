package com.example.brandon.trace;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Periodically checks whether the server is reachable.
 */
public class ServerStatusCheck extends Thread {

    private Activity activity;
    private MenuItem syncButton;
    private boolean isReachable;

    public ServerStatusCheck(Activity activity, MenuItem syncButton) {
        this.activity = activity;
        this.syncButton = syncButton;
        this.isReachable = false;
    }

   public void run() {
       final int timeout = 5000;
       Timer timer = new Timer();
       TimerTask task = new TimerTask() {
           @Override
           public void run() {
               try {
                   URL url = new URL("http://192.168.0.9:8080/ping");
                   HttpURLConnection statusConnection = (HttpURLConnection) url.openConnection();
                   statusConnection.setConnectTimeout(timeout);

                   isReachable = (statusConnection.getResponseCode() == HttpURLConnection.HTTP_OK);
               } catch (SocketTimeoutException | ConnectException e) {
                   isReachable = false;
               } catch (IOException e) {
                   Log.e("Ping Task", "Error attempting to connect to server");
                   e.printStackTrace();
               }

               activity.runOnUiThread(new Runnable() {
                    public void run() {
                        syncButton.setEnabled(isReachable);

                        int alpha = syncButton.isEnabled() ? 255 : 130;
                        syncButton.getIcon().setAlpha(alpha);
                    }
               });
           }
       };

       timer.schedule(task, 0, 3000);
    }
}
