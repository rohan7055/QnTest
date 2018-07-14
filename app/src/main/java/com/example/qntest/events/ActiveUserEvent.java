package com.example.qntest.events;

/**
 * Created by rohan on 6/24/2018.
 */

public class ActiveUserEvent {
    private String size;

    public ActiveUserEvent(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }
}
