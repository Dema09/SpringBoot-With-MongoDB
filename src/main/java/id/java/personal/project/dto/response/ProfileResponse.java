package id.java.personal.project.dto.response;

public class ProfileResponse extends UserResponse{
    private String userProfilePicture;

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }
}
