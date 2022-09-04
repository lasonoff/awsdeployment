package ru.yauroff.awsdeployment.model;

public enum Permission {
    USER_READ("users:read"),
    USER_WRITE("users:write"),
    PROJECT_READ("projects:read"),
    PROJECT_WRITE("projects:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
