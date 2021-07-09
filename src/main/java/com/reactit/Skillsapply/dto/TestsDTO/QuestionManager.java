package com.reactit.Skillsapply.dto.TestsDTO;

import com.reactit.Skillsapply.model.Answers;

import java.util.ArrayList;

public class QuestionManager {
    private String id;
    private String question;
    private String questionType;
    private float duration;
    private float level;
    private float points;
    ArrayList<Answers> answers;

    public QuestionManager() {
    }

    public QuestionManager(String id, String question, String questionType, float duration, float level, float points, ArrayList<Answers> answers) {
        this.id = id;
        this.question = question;
        this.questionType = questionType;
        this.duration = duration;
        this.level = level;
        this.points = points;
        this.answers = answers;
    }

    public String getIdQuestion() {
        return id;
    }

    public void setIdQuestion(String idQuestion) {
        this.id = idQuestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Answers> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answers> answers) {
        this.answers = answers;
    }
}
