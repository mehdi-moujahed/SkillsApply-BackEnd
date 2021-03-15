package com.reactit.Skillsapply.model;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "memberships")
public class Membership {

    @Id
    private int id;

    @NotBlank
    private String duration;

    @NotBlank
    private String label;

    @NotBlank
    private double price;

    public Membership() {
    }

    public Membership(String duration, String label, double price) {
        this.duration = duration;
        this.label = label;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Membership{" +
                "id=" + id +
                ", duration='" + duration + '\'' +
                ", label='" + label + '\'' +
                ", price=" + price +
                '}';
    }
}