package id.java.personal.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "user_close_friend")
public class UserCloseFriend {
    @Id
    private String closeFriendId;

    @DBRef
    private DummyUser dummyUser;

    @DBRef
    private List<DummyUser> closeFriendUsers;

    public String getCloseFriendId() {
        return closeFriendId;
    }

    public void setCloseFriendId(String closeFriendId) {
        this.closeFriendId = closeFriendId;
    }

    public DummyUser getDummyUser() {
        return dummyUser;
    }

    public void setDummyUser(DummyUser dummyUser) {
        this.dummyUser = dummyUser;
    }

    public List<DummyUser> getCloseFriendUsers() {
        return closeFriendUsers;
    }

    public void setCloseFriendUsers(List<DummyUser> closeFriendUsers) {
        this.closeFriendUsers = closeFriendUsers;
    }
}
