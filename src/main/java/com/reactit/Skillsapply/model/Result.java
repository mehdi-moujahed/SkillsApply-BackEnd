package com.reactit.Skillsapply.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "Results")
public class Result {

    @Id
    private String id;

    private String managerId;

    private String testId;

    private String userId;

    private ArrayList<ResultAnswers> result;

    private float score;

    private float duration;

    public Result() {
    }

    public Result(String id, String managerId, String testId, String userId,
                  ArrayList<ResultAnswers> result, float score, float duration) {
        this.id = id;
        this.managerId = managerId;
        this.testId = testId;
        this.userId = userId;
        this.result = result;
        this.score = score;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<ResultAnswers> getResult() {
        return result;
    }

    public void setResult(ArrayList<ResultAnswers> result) {
        this.result = result;
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
}
