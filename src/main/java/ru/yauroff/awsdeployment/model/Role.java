package ru.yauroff.awsdeployment.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(Set.of(Permission.USER_READ, Permission.USER_WRITE, Permission.PROJECT_READ, Permission.PROJECT_WRITE)),
    MODERATOR(Set.of(Permission.USER_READ, Permission.PROJECT_READ)),
    USER(Set.of(Permission.PROJECT_READ));

    Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthority() {
        return getPermissions().stream()
                               .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                               .collect(Collectors.toSet());
    }
}
