package com.example.qntest.events;

/**
 * Created by rohan on 6/24/2018.
 */

public class QuizOverEvent {
    private boolean status;

    public QuizOverEvent(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }
}
