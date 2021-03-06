package id.java.personal.project.domain;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Document(collection = "user_data")
public class DummyUser implements Serializable {
    @Id
    private String id;

    private String username;
    private String nickname;
    private String password;
    private String address;
    private String email;
    private String phoneNumber;
    private String profilePicture;
    private boolean isProtectedAccount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date dateOfBirth;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date lastModified;

    @DBRef
    private DummyUserRole dummyUserRole;

    public DummyUser(){}


    public DummyUser(String username, String nickname, String password, String address, String email, String phoneNumber, Date dateOfBirth, DummyUserRole dummyUserRole) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.dummyUserRole = dummyUserRole;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public DummyUserRole getDummyUserRole() {
        return dummyUserRole;
    }

    public void setDummyUserRole(DummyUserRole dummyUserRole) {
        this.dummyUserRole = dummyUserRole;
    }

    public boolean isProtectedAccount() {
        return isProtectedAccount;
    }

    public void setProtectedAccount(boolean protectedAccount) {
        isProtectedAccount = protectedAccount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DummyUser dummyUser = (DummyUser) o;
        return id.equals(dummyUser.id) && username.equals(dummyUser.username) && Objects.equals(nickname, dummyUser.nickname) && password.equals(dummyUser.password) && address.equals(dummyUser.address) && email.equals(dummyUser.email) && Objects.equals(phoneNumber, dummyUser.phoneNumber) && Objects.equals(profilePicture, dummyUser.profilePicture) && dateOfBirth.equals(dummyUser.dateOfBirth) && createdAt.equals(dummyUser.createdAt) && lastModified.equals(dummyUser.lastModified) && dummyUserRole.equals(dummyUser.dummyUserRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, nickname, password, address, email, phoneNumber, profilePicture, dateOfBirth, createdAt, lastModified, dummyUserRole);
    }
}

