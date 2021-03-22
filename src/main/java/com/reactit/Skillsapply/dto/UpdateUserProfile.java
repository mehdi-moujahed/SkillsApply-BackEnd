package com.reactit.Skillsapply.dto;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateUserProfile {

    @NotBlank(message = "The new firstName cannot be blank")
    private String firstName;

    @NotBlank(message = "The new lastName cannot be blank")
    private String lastName;

    @NotBlank(message = "The new address cannot be blank")
    private String address;

    @NotBlank(message = "The new email cannot be blank")
    private String email;

    private String img;

    public UpdateUserProfile() {
    }

    public UpdateUserProfile(String firstName, String lastName, String address, String email, String img) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.img = img;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
