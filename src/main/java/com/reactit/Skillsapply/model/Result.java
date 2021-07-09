package com.reactit.Skillsapply.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

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

    private Date createdAt;



    public Result() {
    }

    public Result(String id, String managerId, String testId, String userId,
                  ArrayList<ResultAnswers> result, float score, float duration,
                  Date createdAt) {
        this.id = id;
        this.managerId = managerId;
        this.testId = testId;
        this.userId = userId;
        this.result = result;
        this.score = score;
        this.duration = duration;
        this.createdAt = createdAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
