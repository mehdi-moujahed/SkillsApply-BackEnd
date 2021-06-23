package com.reactit.Skillsapply.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import java.util.Date;

@Document(collection = "users")
public class Manager {

    @Id
    private String id;

    @NotBlank(message = "Manager FirstName cannot be blank or null")
    private String firstName;

    @NotBlank(message = "Manager LastName cannot be blank or null")
    private String lastName;

    //    @Indexed(unique = true)
    @Email(message = "Address Mail Not Valid")
    @Column(unique = true)
    @NotBlank(message = "Manager Email cannot be blank or null")
    private String email;

    @NotBlank
    private String phoneNumber;



    private String img;

    @NotBlank
    private String address;

    private boolean emailVerified;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;

    @NotBlank
    private String roles;

    @NotBlank
    private String companyName;

    private Date createdAt;

    private String token;

    private Date CreatedAtToken;

    public Manager() {
    }

    public Manager(String id, String firstName, String lastName, String email, String phoneNumber,
                   String img, String address, boolean emailVerified, String password, String roles,
                   String companyName, Date createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.img = img;
        this.address = address;
        this.emailVerified = emailVerified;
        this.password = password;
        this.roles = roles;
        this.companyName = companyName;
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreatedAtToken() {
        return CreatedAtToken;
    }

    public void setCreatedAtToken(Date createdAtToken) {
        CreatedAtToken = createdAtToken;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", img='" + img + '\'' +
                ", address='" + address + '\'' +
                ", emailVerified=" + emailVerified +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }
}
