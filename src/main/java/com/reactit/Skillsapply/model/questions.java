package com.reactit.Skillsapply.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class questions {

    @Id
    private int id;

    @NotBlank
    private String question;

    @NotBlank
    private List<String> answers;

    @NotBlank
    private String type;

    public questions() {
    }

    public questions(@NotBlank String question, @NotBlank List<String> answers, @NotBlank String type) {
        this.question = question;
        this.answers = answers;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
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
                ", answers=" + answers +
                ", type='" + type + '\'' +
                '}';
    }
}
