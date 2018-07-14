package com.example.qntest.events;

/**
 * Created by rohan on 6/24/2018.
 */

public class ConnectivityEvent {
    private boolean isConnected;

    public ConnectivityEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
