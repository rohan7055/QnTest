package com.example.qntest.events;

/**
 * Created by rohan on 6/24/2018.
 */

public class StartQuizEvent {
    private boolean start;

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public StartQuizEvent(boolean start) {
        this.start = start;
    }
}
