package com.reactit.Skillsapply.dto;

import com.reactit.Skillsapply.model.Answers;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class QuestionDTO {

    private String question;

    private ArrayList<Answers> answers;


    public QuestionDTO() {
    }

    public QuestionDTO(String question, ArrayList<Answers> answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Answers> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answers> answers) {
        this.answers = answers;
    }

//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
}
