package com.reactit.Skillsapply.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.util.Date;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Document(collection = "users")
@Component
public class User  {


    @Id
    private String id;

//    @NotNull(message = "User's first name must not be null")
    @NotBlank(message = "Candidate FirstName cannot be blank or null")
    private String firstName;

    @NotBlank(message = "Candidate LastName cannot be blank or null")
    private String lastName;

//    @Indexed(unique = true)
    @Email(message = "Address Mail Not Valid")
    @Column(unique = true)
    @NotBlank(message = "Candidate Email cannot be blank or null")
    private String email;

    @NotBlank(message = "Candidate Phone Number cannot be blank or null")
    private String phoneNumber;

    private Date birthDate;

    private String img;

    private String address;

    @NotBlank(message = "manager ID cannot be blank or null")
    private String managerID;

    private boolean emailVerified;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    private String roles;

    private int diploma;

    private Date createdAt;

    public User() {
    }

    public User(String id, String firstName, String lastName, String email, String phoneNumber,
                Date birthDate, String img, String address, String managerID, boolean emailVerified,
                String password, String roles, int diploma, Date createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.img = img;
        this.address = address;
        this.managerID = managerID;
        this.emailVerified = emailVerified;
        this.password = password;
        this.roles = roles;
        this.diploma = diploma;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getDiploma() {
        return diploma;
    }

    public void setDiploma(int diploma) {
        this.diploma = diploma;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getManagerID() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", birthDate=" + birthDate +
                ", img='" + img + '\'' +
                ", address='" + address + '\'' +
                ", emailVerified=" + emailVerified +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }

}