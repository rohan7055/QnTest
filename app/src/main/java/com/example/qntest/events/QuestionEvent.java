package com.example.qntest.events;

import com.example.qntest.restapi.models.QuestionModel;

/**
 * Created by rohan on 6/24/2018.
 */

public class QuestionEvent {
    QuestionModel questionModel;

    public QuestionEvent(QuestionModel questionModel) {
        this.questionModel = questionModel;
    }

    public QuestionModel getQuestionModel() {
        return questionModel;
    }
}
