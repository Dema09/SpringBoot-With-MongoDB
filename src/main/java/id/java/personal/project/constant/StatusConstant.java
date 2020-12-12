package id.java.personal.project.constant;

public enum StatusConstant {
    INTERNAL_SERVER_ERROR("Internal Server Error"),
    NOT_FOUND("Not Found"),
    OK("Ok"),
    UNAUTHORIZED("Unauthorized"),
    CREATED("Created"),
    BAD_REQUEST("Bad Request");

    private String message;

    StatusConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
