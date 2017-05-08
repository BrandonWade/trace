package com.example.brandon.trace;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

/**
 * Contains utility functions for the UI.
 */
public class UIUtils {

    public static MainActivity mainActivity;

    public static final int SHORT_TOAST_DURATION = 1000;

    public static void setMainActivity(MainActivity main) {
        mainActivity = main;
    }

    public static void toggleConfirmButton(final boolean enabled) {
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
            int alpha = enabled ? 255 : 130;
            mainActivity.confirmButton.getIcon().setAlpha(alpha);
            mainActivity.confirmButton.setEnabled(enabled);
            }
        });
    }

    public static void toggleSyncButton(final boolean enabled) {
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                int alpha = enabled ? 255 : 130;
                mainActivity.syncButton.getIcon().setAlpha(alpha);
                mainActivity.syncButton.setEnabled(enabled);
            }
        });
    }

    public static void showToast(final int message, final int duration) {
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(mainActivity.getApplicationContext(), message, Toast.LENGTH_SHORT);

                CountDownTimer toastTimer = new CountDownTimer(duration, duration) {
                    @Override
                    public void onTick(long millisUntilFinished) {}

                    @Override
                    public void onFinish() {
                        toast.cancel();
                    }
                };

                toast.show();
                toastTimer.start();
            }
        });
    }
}
