package id.java.personal.project.constant;

public enum AppEnum {
    SUCCESS_REGISTER_USER("Successfully Register User's Data!"),
    USER_DATA_NOT_FOUND("Your User Data is Not Found!"),
    DATE_FORMAT("dd/MM/yyyy"),
    SUCCESS_UPDATED_USER_DATA_PROFILE("Successfully Updating Profile with Id: "),
    IMAGE_NOT_FOUND_OR_CORRUPTED("Can't retrieve your image, or image might be corrupted!"),
    INCORRECT_USERNAME("Incorrect username. Please input your valid username!"),
    INCORRECT_PASSWORD("Incorrect Password. Please input your valid password!"),
    ALPHA_NUMERIC_REGEX("ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvwxyz"),
    THIS_USER_WITH_USERNAME("This user with username: "),
    USER_WITH_ID("This user with id: "),
    HAS_BEEN_EXISTS(" has been exists!"),
    IS_NOT_EXISTS(" is not exists!"),
    SUCCESSFULLY_FOLLOWED_USER_WITH_USERNAME("Successfully followed user with username: "),
    YOU_CAN_NOT_FOLLOW_YOURSELF("You can't follow yourself!"),
    ALREADY_EXISTS_IN_FOLLOWER_OR_FOLLOWING_LIST("Already exists in followers and followings list!"),
    SUCCESSFULLY_UNFOLLOWED_USER_WITH_USERNAME("Successfully unfollowed user with username: "),
    CANNOT_UNFOLLOW_YOURSELF("You can't unfollow yourself!");

    private String message;

    AppEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
