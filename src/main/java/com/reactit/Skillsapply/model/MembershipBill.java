package com.reactit.Skillsapply.model;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class MembershipBill {

    @Id
    private int id;

    @NotBlank
    private double amount;

    private int managerID;

    @DateTimeFormat
    private LocalDate nextPayment;

    public MembershipBill() {
    }

    public MembershipBill(@NotBlank double amount, int managerID, LocalDate nextPayment) {
        this.amount = amount;
        this.managerID = managerID;
        this.nextPayment = nextPayment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public LocalDate getNextPayment() {
        return nextPayment;
    }

    public void setNextPayment(LocalDate nextPayment) {
        this.nextPayment = nextPayment;
    }

    @Override
    public String toString() {
        return "MembershipBill{" +
                "id=" + id +
                ", amount=" + amount +
                ", managerID=" + managerID +
                ", nextPayment=" + nextPayment +
                '}';
    }
}
