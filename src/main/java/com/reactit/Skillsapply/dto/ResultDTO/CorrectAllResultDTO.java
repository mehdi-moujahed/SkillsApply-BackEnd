package com.reactit.Skillsapply.dto.ResultDTO;

import com.reactit.Skillsapply.model.User;

import java.util.ArrayList;

public class CorrectAllResultDTO {
    private String idTest;
    private String name;
    private String description;
    private float duration;
    private float durationSpent;
    private float score;
    private float scoreCollected;
    private int scorePercentage;
    private float level;
    ArrayList<CorrectQuestionDTO> correctQuestion= new ArrayList<>();
    private User user;

    public CorrectAllResultDTO() {
    }

    public CorrectAllResultDTO(String idTest, String name, String description, float duration, float durationSpent, float score, float scoreCollected, int scorePercentage, float level, ArrayList<CorrectQuestionDTO> correctQuestion, User user) {
        this.idTest = idTest;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.durationSpent = durationSpent;
        this.score = score;
        this.scoreCollected = scoreCollected;
        this.scorePercentage = scorePercentage;
        this.level = level;
        this.correctQuestion = correctQuestion;
        this.user = user;
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

    public float getDurationSpent() {
        return durationSpent;
    }

    public void setDurationSpent(float durationSpent) {
        this.durationSpent = durationSpent;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getScoreCollected() {
        return scoreCollected;
    }

    public void setScoreCollected(float scoreCollected) {
        this.scoreCollected = scoreCollected;
    }

    public int getScorePercentage() {
        return scorePercentage;
    }

    public void setScorePercentage(int scorePercentage) {
        this.scorePercentage = scorePercentage;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    public ArrayList<CorrectQuestionDTO> getCorrectQuestion() {
        return correctQuestion;
    }

    public void setCorrectQuestion(ArrayList<CorrectQuestionDTO> correctQuestion) {
        this.correctQuestion = correctQuestion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
