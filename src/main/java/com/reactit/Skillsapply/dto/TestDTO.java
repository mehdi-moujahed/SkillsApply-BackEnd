package com.reactit.Skillsapply.dto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class TestDTO {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private float score;

    @NotNull
    private float duration;

    @NotNull
    private float level;

    @NotNull
    private float rate;

    @NotNull
    private ArrayList<QuestionDTO> questions;

    public TestDTO() {
    }

    public TestDTO(String name, String description, float score, float duration, float level, float rate,  ArrayList<QuestionDTO> questions) {
        this.name = name;
        this.description = description;
        this.score = score;
        this.duration = duration;
        this.level = level;                                 
        this.rate = rate;
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
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

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public  ArrayList<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionDTO> questions) {
        this.questions = questions;
    }
}
