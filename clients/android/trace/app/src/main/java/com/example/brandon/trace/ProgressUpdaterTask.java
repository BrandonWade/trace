package com.example.brandon.trace;

import android.os.Handler;
import android.os.Looper;


/**
 * Updates the UI with the current status of files.
 */
public class ProgressUpdaterTask extends Thread {

    private Handler redrawHandler;
    private boolean isComplete;

    public static long uiUpdatePeriod = 300;

    public ProgressUpdaterTask() {
        this.redrawHandler = new Handler(Looper.getMainLooper());
    }

    private Runnable updateUI = new Runnable() {
        @Override public void run() {
            FileUtils.fileListAdapter.notifyDataSetChanged();

            if (isComplete) {
                redrawHandler.removeCallbacks(updateUI);
            } else {
                redrawHandler.postDelayed(updateUI, uiUpdatePeriod);
            }
        }
    };

    public void run() {
        redrawHandler.post(updateUI);
    }

    public void complete() {
        this.isComplete = true;
    }
}
