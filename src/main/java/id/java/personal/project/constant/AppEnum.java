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
    CANNOT_UNFOLLOW_YOURSELF("You can't unfollow yourself!"),
    SUCCESSFULLY_SET_YOUR_ACCOUNT_TO_PROTECTED("Successfully set your account to protected!"),
    YOUR_ACCOUNT_ALREADY_PROTECTED("Your account is already protected!"),
    YOUR_ACCOUNT_ALREADY_UNPROTECTED("Your account is already un-protected!"),
    SUCCESSFULLY_SET_YOUR_ACCOUNT_TO_UNPROTECTED("Successfully set your account to un-protected!"),
    SUCCESSFULLY_ADDED_CLOSE_FRIEND("Successfully added your close friend!"),
    SUCCESSFULLY_REMOVE_CLOSE_FRIEND("Successfully remove your close friend!"),
    OLD_PASSWORD_SAME_AS_NEW_PASSWORD("Your old password is same as your new password. Please consider to add another password!"),
    YOUR_PASSWORD_DOES_NOT_MATCH_WITH_A_NEW_PASSWORD("Your password doesn't match with your new password!"),
    SUCCESSFULLY_UPDATE_YOUR_PASSWORD("Successfully update your password!"),
    INCORRECT_OLD_PASSWORD("Your old password is incorrect!"),
    SUCCESSFULLY_ADD_USER_TO_BLOCK_LIST("Successfully added user into your blocklist!"),
    YOU_CANNOT_BLOCK_YOURSELF("You can't block yourself!"),
    NO_USER_FOUND_TO_UNBLOCK("No user found to unblock! Please check again!"),
    SUCCESSFULLY_REMOVE_USER_FROM_BLOCK_USER_WITH_ID("Successfully remove user from block user with id: "),
    YOU_ALREADY_BLOCK_THIS_USER_WITH_ID("You already block this user with id: "),
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret");

    private String message;

    AppEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
