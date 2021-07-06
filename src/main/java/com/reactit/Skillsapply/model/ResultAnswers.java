package com.reactit.Skillsapply.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;

public class ResultAnswers {

    @Id
    private String id;

    private String questionId;

    private ArrayList<String > answersId;

    public ResultAnswers() {
    }

    public ResultAnswers(String id, String questionId, ArrayList<String> answersId) {
        this.id = id;
        this.questionId = questionId;
        this.answersId = answersId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public ArrayList<String> getAnswersId() {
        return answersId;
    }

    public void setAnswersId(ArrayList<String> answersId) {
        this.answersId = answersId;
    }
}
