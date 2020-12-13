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
    HAS_BEEN_EXISTS(" has been exists!");

    private String message;

    AppEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
