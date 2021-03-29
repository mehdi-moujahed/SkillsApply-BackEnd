package com.reactit.Skillsapply.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Questions")
public class Questions {

    @Id
    private String id;

//    @NotBlank
    private String question;

//    @NotEmpty
//    @OneToMany
    private ArrayList<String> answersID;


    private String type;

    public Questions() {
    }

    public Questions( @NotBlank String question, @NotBlank ArrayList<String> answersID, @NotBlank String type) {

        this.question = question;
        this.answersID = answersID;
        this.type = type;
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

    public ArrayList<String> getAnswers() {
        return this.answersID;
    }

    public void setAnswers(ArrayList<String> answersID) {
        this.answersID = answersID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "questions{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answers=" + answersID +
                ", type='" + type + '\'' +
                '}';
    }
}
