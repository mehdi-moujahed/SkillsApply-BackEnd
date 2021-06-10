package com.reactit.Skillsapply.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Questions")
public class Questions {

    @Id
    private String id;

    @NotNull
    private String question;

    @NotNull
    private ArrayList<String> answersID;

    @NotNull
    private String questionType;

    @NotNull
    private float duration;

    @NotNull
    private float level;

    @NotNull
    private float points;

    public Questions() {
    }

    public Questions(String id, String question, ArrayList<String> answersID, @NotNull String questionType,
                     @NotNull float duration, @NotNull float level,
                     @NotNull float points) {
        this.id = id;
        this.question = question;
        this.answersID = answersID;
        this.questionType = questionType;
        this.duration = duration;
        this.level = level;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswersID() {
        return answersID;
    }

    public void setAnswersID(ArrayList<String> answersID) {
        this.answersID = answersID;
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
}
