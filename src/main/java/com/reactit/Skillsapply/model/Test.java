package com.reactit.Skillsapply.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;

@Document(collection = "Tests")
public class Test {

    @Id
    private String id;

    @NotNull
    private String name;

    private String technologyLogo;

    @NotNull
    private String description;

    @NotNull
    private float duration;

    private float score;

    private float level;

    @NotNull
    private float rate;

    private Date createdAt;

    private ArrayList<String> questionsID;

    private String managerID;

    @NotNull
    private boolean premiumPack;

    private int nbrOfRate;

    private float totalRates;

    public Test() {
    }

    public Test(String id, @NotNull String name, String technologyLogo, @NotNull String description,
                @NotNull float duration, float score, float level, @NotNull float rate, Date createdAt,
                ArrayList<String> questionsID, String managerID, @NotNull boolean premiumPack, int nbrOfRate, float totalRates) {
        this.id = id;
        this.name = name;
        this.technologyLogo = technologyLogo;
        this.description = description;
        this.duration = duration;
        this.score = score;
        this.level = level;
        this.rate = rate;
        this.createdAt = createdAt;
        this.questionsID = questionsID;
        this.managerID = managerID;
        this.premiumPack = premiumPack;
        this.nbrOfRate = nbrOfRate;
        this.totalRates = totalRates;
    }

    public float getTotalRates() {
        return totalRates;
    }

    public void setTotalRates(float totalRates) {
        this.totalRates = totalRates;
    }

    public int getNbrOfRate() {
        return nbrOfRate;
    }

    public void setNbrOfRate(int nbrOfRate) {
        this.nbrOfRate = nbrOfRate;
    }

    public boolean isPremiumPack() {
        return premiumPack;
    }

    public void setPremiumPack(boolean premiumPack) {
        this.premiumPack = premiumPack;
    }

    public String getManagerID() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTechnologyLogo() {
        return technologyLogo;
    }

    public void setTechnologyLogo(String technologyLogo) {
        this.technologyLogo = technologyLogo;
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

    public ArrayList<String> getQuestionsID() {
        return questionsID;
    }

    public void setQuestionsID(ArrayList<String> questionsID) {
        this.questionsID = questionsID;
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

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", technologyLogo='" + technologyLogo + '\'' +
                ", description='" + description + '\'' +
                ", duration='" + duration + '\'' +
                ", score=" + score +
                ", level=" + level +
                ", rate=" + rate +
                ", questionsID=" + questionsID +
                '}';
    }
}
