package com.reactit.Skillsapply.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

public class Answers {

    @Id
    private String id;

    @NotNull
    private String answer;

    @NotNull
    private boolean status;


    public Answers() {
    }

    public Answers(String id, @NotNull String answer, @NotNull boolean status) {
        this.id = id;
        this.answer = answer;
        this.status = status;
    }

    public String  getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
