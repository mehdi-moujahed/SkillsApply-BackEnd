package com.reactit.Skillsapply.dto.ResultDTO;

import com.reactit.Skillsapply.dto.TestsDTO.QuestionManager;
import com.reactit.Skillsapply.model.Questions;

public class CorrectQuestionDTO {
    private QuestionManager question;
    private Boolean correct;

    public CorrectQuestionDTO() {
    }

    public CorrectQuestionDTO(QuestionManager question, Boolean correct) {
        this.question = question;
        this.correct = correct;
    }

    public QuestionManager getQuestion() {
        return question;
    }

    public void setQuestion(QuestionManager question) {
        this.question = question;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
