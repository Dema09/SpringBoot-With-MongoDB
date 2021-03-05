package id.java.personal.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowerAndFollowing that = (FollowerAndFollowing) o;
        return followId.equals(that.followId) && dummyUser.equals(that.dummyUser) && followers.equals(that.followers) && followings.equals(that.followings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followId, dummyUser, followers, followings);
    }
}
