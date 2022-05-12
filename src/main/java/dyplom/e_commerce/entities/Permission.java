package dyplom.e_commerce.entities;

public enum Permission {
    CUSTOMER("customer"),
    USER("user"),
    ADMIN("admin");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
