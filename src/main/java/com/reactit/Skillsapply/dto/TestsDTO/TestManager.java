package com.reactit.Skillsapply.dto.TestsDTO;

import java.util.ArrayList;

public class TestManager {
    private String idTest;
    private String name;
    private String description;
    private float duration;
    private float score;
    private float level;
    ArrayList<QuestionManager> questions;

    public TestManager() {
    }

    public TestManager(String idTest, String name, String description, float duration, float score, float level, ArrayList<QuestionManager> questions) {
        this.idTest = idTest;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.score = score;
        this.level = level;
        this.questions = questions;
    }

    public String getIdTest() {
        return idTest;
    }

    public void setIdTest(String idTest) {
        this.idTest = idTest;
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

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    public ArrayList<QuestionManager> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionManager> questions) {
        this.questions = questions;
    }
}
