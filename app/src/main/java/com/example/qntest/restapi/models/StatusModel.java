package com.example.qntest.restapi.models;

import java.io.Serializable;

/**
 * Created by rohan on 6/24/2018.
 */

public class StatusModel implements Serializable {
    private boolean status;
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
