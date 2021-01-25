package id.java.personal.project.dto.response;

import java.util.List;

public class FollowerResponse {
    private List<FollowerOrFollowingResponse> followers;

    public List<FollowerOrFollowingResponse> getFollowers() {
        return followers;
    }

    public void setFollowers(List<FollowerOrFollowingResponse> followers) {
        this.followers = followers;
    }
}
