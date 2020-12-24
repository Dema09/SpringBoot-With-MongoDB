package id.java.personal.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "following")
public class Following {
    @Id
    private String followingId;

    @DBRef
    private DummyUser userId;

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }

    public DummyUser getUserId() {
        return userId;
    }

    public void setUserId(DummyUser userId) {
        this.userId = userId;
    }
}
