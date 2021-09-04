package ru.yauroff.awsdeployment.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.yauroff.awsdeployment.model.Status;
import ru.yauroff.awsdeployment.model.User;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class SecurityUser implements UserDetails {
    private String username;
    private String password;
    private List<SimpleGrantedAuthority> authority;
    private boolean isActive;

    public static UserDetails fromUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(),
                user.getStatus()
                    .equals(Status.ACTIVE),
                user.getStatus()
                    .equals(Status.ACTIVE),
                user.getStatus()
                    .equals(Status.ACTIVE),
                user.getStatus()
                    .equals(Status.ACTIVE),
                user.getRole()
                    .getAuthority()
        );

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authority;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}

