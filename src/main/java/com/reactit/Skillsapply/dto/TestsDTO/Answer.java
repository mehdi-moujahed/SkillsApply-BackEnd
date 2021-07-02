package com.reactit.Skillsapply.dto.TestsDTO;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

public class Answer {

    private String id;

    private String answer;

    public Answer() {
    }

    public Answer(String id, String answer) {
        this.id = id;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
