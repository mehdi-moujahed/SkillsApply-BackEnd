package com.reactit.Skillsapply.dto;

import com.reactit.Skillsapply.model.Answers;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class QuestionDTO {

    @NotNull
    private String question;

    private ArrayList<Answers> answers;

    @NotNull
    private String questionType;

    @NotNull
    private float duration;

    @NotNull
    private float level;

    @NotNull
    private float points;

    private String uniqueAnswer;


    public QuestionDTO() {
    }

    public QuestionDTO(@NotNull String question,  ArrayList<Answers> answers, @NotNull String questionType,
                       @NotNull float duration, @NotNull float level, @NotNull float points,
                       String uniqueAnswer) {
        this.question = question;
        this.answers = answers;
        this.questionType = questionType;
        this.duration = duration;
        this.level = level;
        this.points = points;
        this.uniqueAnswer = uniqueAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Answers> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answers> answers) {
        this.answers = answers;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public String getUniqueAnswer() {
        return uniqueAnswer;
    }

    public void setUniqueAnswer(String uniqueAnswer) {
        this.uniqueAnswer = uniqueAnswer;
    }
}
