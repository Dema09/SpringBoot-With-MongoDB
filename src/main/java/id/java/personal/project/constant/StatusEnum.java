package id.java.personal.project.constant;

public enum StatusEnum {
    INTERNAL_SERVER_ERROR("Internal Server Error"),
    NOT_FOUND("Not Found"),
    OK("Ok"),
    UNAUTHORIZED("Unauthorized"),
    CREATED("Created"),
    BAD_REQUEST("Bad Request"),
    CONFLICT("Conflict"),
    NO_CONTENT("No Content"),
    NOT_MODIFIED("Not Modified");

    private String message;

    StatusEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
