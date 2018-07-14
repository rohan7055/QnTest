package com.example.qntest.restapi.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohan on 6/24/2018.
 */

public class QuestionModel {

    private int quiz_id;
    private String ques_id;
    private String question;
    private String question_category;
    private List<String> options;
    private int answer;
    private List<String> user_attempt_correct;
    private List<String> user_attempt_incorrect;

    public QuestionModel(int quiz_id, String ques_id, String question, String question_category, List<String> options, int answer) {
        this.quiz_id = quiz_id;
        this.ques_id = ques_id;
        this.question = question;
        this.question_category = question_category;
        this.options = options;
        this.answer = answer;
    }

    public String getQues_id() {
        return ques_id;
    }

    public void setQues_id(String ques_id) {
        this.ques_id = ques_id;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion_category() {
        return question_category;
    }

    public void setQuestion_category(String question_category) {
        this.question_category = question_category;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public List<String> getUser_attempt_correct() {
        return user_attempt_correct;
    }

    public void setUser_attempt_correct(List<String> user_attempt_correct) {
        this.user_attempt_correct = user_attempt_correct;
    }

    public List<String> getUser_attempt_incorrect() {
        return user_attempt_incorrect;
    }

    public void setUser_attempt_incorrect(List<String> user_attempt_incorrect) {
        this.user_attempt_incorrect = user_attempt_incorrect;
    }
    /*var questionSchema=new Schema({
            ques_id:Number,
    quiz_id:Number,
    question_category:String,
    question:{type:String,required:true},
    options:{type:[String],required:true},
    answer:{type:Number,required:true},
    user_attempt_correct:{type:[String]},
    user_attempt_incorrect:{type:[String]}
});*/
}
