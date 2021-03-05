package id.java.personal.project.dto.response;

import id.java.personal.project.domain.DummyUser;

import java.util.List;

public class DiscoverPeopleResponse {
    private String userId;
    private Integer numberOfMutualUser;
    private List<DummyUser> mutualUsers;
    private String username;
    private String userRole;
    private Integer userAge;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getNumberOfMutualUser() {
        return numberOfMutualUser;
    }

    public void setNumberOfMutualUser(Integer numberOfMutualUser) {
        this.numberOfMutualUser = numberOfMutualUser;
    }

    public List<DummyUser> getMutualUsers() {
        return mutualUsers;
    }

    public void setMutualUsers(List<DummyUser> mutualUsers) {
        this.mutualUsers = mutualUsers;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }
}
