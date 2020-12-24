package id.java.personal.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "follower")
public class Follower {
    @Id
    private String followerId;

    @DBRef
    private DummyUser userId;

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public DummyUser getUserId() {
        return userId;
    }

    public void setUserId(DummyUser userId) {
        this.userId = userId;
    }
}
