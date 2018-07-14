package com.example.qntest.restapi.models;

import java.io.Serializable;

/**
 * Created by rohan on 6/24/2018.
 */

public class QuizModel implements Serializable {
    /* data:{quiz_id:docs[0].quiz_id,
           quiz_category:docs[0].quiz_category,
           created_at:docs[0].created_at,
           updated_at:docs[0].updated_at
   },*/


    private String quiz_id;
    private String quiz_category;
    private String created_at;
    private String updated_at;

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuiz_category() {
        return quiz_category;
    }

    public void setQuiz_category(String quiz_category) {
        this.quiz_category = quiz_category;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
