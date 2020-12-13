package id.java.personal.project.dto.response;

import java.util.List;

public class AllUserResponse {
    private List<UserResponseWithAge> users;

    public List<UserResponseWithAge> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponseWithAge> users) {
        this.users = users;
    }
}
