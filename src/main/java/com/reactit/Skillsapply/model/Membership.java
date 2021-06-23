package com.reactit.Skillsapply.model;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "memberships")
public class Membership {

//    private static int instanceCounter = 0;

    @Id
    private String id;

    private String duration;

    @NotBlank
    private String label;

    private double price;

    @NotBlank
    private String description;


    public Membership() {
    }

    public Membership(String duration, String label, double price, String description) {
        this.duration = duration;
        this.label = label;
        this.price = price;
        this.description = description;
//        instanceCounter++;
    }

//    public static int getInstanceCounter() {
//        return instanceCounter;
//    }
//
//    public static void setInstanceCounter(int instanceCounter) {
//        Membership.instanceCounter = instanceCounter;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
