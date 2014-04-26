package com.rs2.util;

import com.rs2.GlobalVariables;

/**
 *
 */
public class ShutdownWorldProcess implements Runnable {

    private final long sleepTime;

    public ShutdownWorldProcess(int seconds) {
        sleepTime = seconds*1000;
    }
    
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace(); 
        }
        GlobalVariables.ACCEPT_CONNECTIONS = false;
        System.exit(0);
    }
}
