package ru.yauroff.awsdeployment.dto;

import lombok.Data;
import ru.yauroff.awsdeployment.model.Role;
import ru.yauroff.awsdeployment.model.Status;
import ru.yauroff.awsdeployment.model.User;

@Data
public class UserRequestDTO {
    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
    private Status status;

    public static User toUser(UserRequestDTO userRequestDTO) {
        User user = new User(userRequestDTO.getId(), userRequestDTO.getLogin(),
                userRequestDTO.getPassword(),
                userRequestDTO.getFirstName(), userRequestDTO.getLastName(), userRequestDTO.getRole(),
                userRequestDTO.getStatus());
        return user;
    }

    public User toUser() {
        return UserRequestDTO.toUser(this);
    }

    public void updateUser(User user) {
        if (getPassword() != null) {
            user.setPassword(getPassword());
        }
        if (getFirstName() != null) {
            user.setFirstName(getFirstName());
        }
        if (getLastName() != null) {
            user.setLastName(getLastName());
        }
        if (getRole() != null) {
            user.setRole(getRole());
        }
        if (getStatus() != null) {
            user.setStatus(getStatus());
        }
    }
}
