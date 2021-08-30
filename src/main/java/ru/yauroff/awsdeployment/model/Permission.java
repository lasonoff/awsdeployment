package ru.yauroff.awsdeployment.model;

public enum Permission {
    USER_READ("users:read"),
    USER_WRITE("users:write");

    private String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
