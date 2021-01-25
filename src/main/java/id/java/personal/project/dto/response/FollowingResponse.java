package id.java.personal.project.dto.response;

import java.util.List;

public class FollowingResponse {
    private List<FollowerOrFollowingResponse> followings;

    public List<FollowerOrFollowingResponse> getFollowings() {
        return followings;
    }

    public void setFollowings(List<FollowerOrFollowingResponse> followings) {
        this.followings = followings;
    }
}
