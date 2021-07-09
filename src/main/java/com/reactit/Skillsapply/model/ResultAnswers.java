package com.reactit.Skillsapply.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;

public class ResultAnswers {

    @Id
    private String id;

    private String questionId;

    private ArrayList<String > answersId;

    private String answer;

    private String questionType;


    public ResultAnswers() {
    }

    public ResultAnswers(String id, String questionId, ArrayList<String> answersId, String answer, String questionType) {
        this.id = id;
        this.questionId = questionId;
        this.answersId = answersId;
        this.answer = answer;
        this.questionType = questionType;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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
