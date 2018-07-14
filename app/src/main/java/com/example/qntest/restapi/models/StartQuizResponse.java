package com.example.qntest.restapi.models;

import java.io.Serializable;

/**
 * Created by rohan on 6/24/2018.
 */

public class StartQuizResponse extends StatusModel implements Serializable {


  private QuizModel data;

    public QuizModel getData() {
        return data;
    }

    public void setData(QuizModel data) {
        this.data = data;
    }
}
