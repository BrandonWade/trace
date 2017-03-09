package com.example.brandon.trace;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Periodically attempt to reestablish the ControlConnected if it is not already connected.
 */
public class ControlConnectionManager extends Thread {

    public void run() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ControlConnection controlConn = ControlConnection.getInstance();
                if (!controlConn.isReachable()) {
                    controlConn.connect();
                }
            }
        };

        timer.schedule(task, 0, 3000);
    }
}
