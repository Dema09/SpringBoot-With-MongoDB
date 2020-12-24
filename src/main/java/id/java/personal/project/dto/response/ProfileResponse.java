package id.java.personal.project.dto.response;

public class ProfileResponse extends UserResponse{
    private String userProfilePicture;
    private Integer numberOfFollowers;
    private Integer numberOfFollowings;

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    public Integer getNumberOfFollowers() {
        return numberOfFollowers;
    }

    public void setNumberOfFollowers(Integer numberOfFollowers) {
        this.numberOfFollowers = numberOfFollowers;
    }

    public Integer getNumberOfFollowings() {
        return numberOfFollowings;
    }

    public void setNumberOfFollowings(Integer numberOfFollowings) {
        this.numberOfFollowings = numberOfFollowings;
    }
}
