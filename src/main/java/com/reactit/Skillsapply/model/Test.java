package com.reactit.Skillsapply.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

public class Test {

    @Id
    private int id;

    @NotBlank
    private String name;

    @NotBlank
    private String technology;

    @NotBlank
    private String technologyLogo;

    @NotBlank
    private String description;

    @NotBlank
    private String duration;

    @NotBlank
    private int score;

    public Test() {
    }

    public Test(int id, @NotBlank String name, @NotBlank String technology, @NotBlank String description,
                @NotBlank String duration, int score) {
        this.id = id;
        this.name = name;
        this.technology = technology;
        this.description = description;
        this.duration = duration;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", technology='" + technology + '\'' +
                ", technologyLogo='" + technologyLogo + '\'' +
                ", description='" + description + '\'' +
                ", duration='" + duration + '\'' +
                ", score=" + score +
                '}';
    }
}
