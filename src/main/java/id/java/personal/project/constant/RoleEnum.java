package id.java.personal.project.constant;

public enum RoleEnum {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private String message;

    RoleEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
