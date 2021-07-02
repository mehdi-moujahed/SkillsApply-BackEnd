package com.reactit.Skillsapply.dto.TestsDTO;


import java.util.ArrayList;

public class Test {
    private String idTest;
    private String name;
    private String description;
    private float duration;
    private float score;
    private float level;
    ArrayList<Question> questions;

    public Test() {
    }

    public Test(String idTest, String name, String description, float duration, float score, float level, ArrayList<Question> questions) {
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

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
