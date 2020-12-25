package id.java.personal.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "follower_and_following")
public class FollowerAndFollowing {

    @Id
    private String followId;

    @DBRef
    private DummyUser dummyUser;

    @DBRef
    private List<DummyUser> followers;

    @DBRef
    private List<DummyUser> followings;

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public DummyUser getDummyUser() {
        return dummyUser;
    }

    public void setDummyUser(DummyUser dummyUser) {
        this.dummyUser = dummyUser;
    }

    public List<DummyUser> getFollowers() {
        return followers;
    }

    public void setFollowers(List<DummyUser> followers) {
        this.followers = followers;
    }

    public List<DummyUser> getFollowings() {
        return followings;
    }

    public void setFollowings(List<DummyUser> followings) {
        this.followings = followings;
    }
}
