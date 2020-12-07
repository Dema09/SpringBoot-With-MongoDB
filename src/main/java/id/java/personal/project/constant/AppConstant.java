package id.java.personal.project.constant;

public enum AppConstant {
    SUCCESS_REGISTER_USER("Successfully Register User's Data With Username: "),
    USER_DATA_NOT_FOUND("Your User Data is Not Found!"),
    DATE_FORMAT("yyyy/MM/dd");
    private String message;

    AppConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
