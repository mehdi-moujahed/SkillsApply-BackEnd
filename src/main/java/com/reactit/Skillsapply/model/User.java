package com.reactit.Skillsapply.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.io.Console;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveCallback;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.PrePersist;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//@EntityListeners(BeforeSaveListener.class)
@Document(collection = "users")
@Component
//@EnableMongoAuditing
public class User {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Id
    private String id;
//
//    @NotNull(message = "User's first name must not be null")
    @NotBlank(message = "not blank")
    private String firstName;
//    @NotNull
    private String lastName;

//    @Column(unique = true)
    @Indexed(unique = true)
    private String email;

    private int phoneNumber;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,pattern = "dd-MM-yyyy")
    private Date birthDate;
    private String img;
    private String address;
    private boolean emailVerified;

    @JsonProperty(access = Access.WRITE_ONLY)
//    @ValidPassword
    private String password;
    private List<String> roles;

    public User() {

    }

//    @PrePersist
//    public void getPasswordBeforePersist(){
//        log.info("User Password : "+password);
//    }

    public User(@NotNull(message = "User's first name must not be null") String firstName, @NotNull String lastName, String email, int phoneNumber,
                Date birthDate, String img, String address, boolean emailVerified,
                String password,
                List<String> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.img = img;
        this.address = address;
        this.emailVerified = false;
        this.password = password;
        this.roles = roles;
    }

    //    public User(String firstName, String lastName, String email, String password, List<String> roles) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.password = password;
//        this.roles = roles;
//    }


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

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
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




    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
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

//
//    public User onAfterConvert(User user, org.bson.Document document ,String s) {
//
//    }

//    @Override
//    public User onBeforeConvert(User user, String s) {
//        System.out.println("onBeforeConvert "+user.getPassword());
//        user.setPassword( passwordEncoder.encode(user.getPassword()));
//        return user;
//    }


//    @Override
//    public User onBeforeSave(User user, org.bson.Document document, String s) {
//        System.out.println("onBeforeConvert "+user.getPassword());
//
//        user.setPassword( passwordEncoder.encode(user.getPassword()));
//        return user;
//    }

//    @Override
//    public User onBeforeSave(User user, String s) {
//        System.out.println("onBeforeConvert "+user.getPassword());
//        user.setPassword( passwordEncoder.encode(user.getPassword()));
//        return user;
//    }


//    public User onBeforeConvert(User user, String s) {
//        System.out.println("onBeforeSave "+user.getPassword());
//
//        user.setPassword( passwordEncoder.encode(user.getPassword()));
//        return user;
//    }
}