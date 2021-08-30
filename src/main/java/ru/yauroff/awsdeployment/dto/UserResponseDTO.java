package ru.yauroff.awsdeployment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yauroff.awsdeployment.model.Role;
import ru.yauroff.awsdeployment.model.Status;
import ru.yauroff.awsdeployment.model.User;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
public class UserResponseDTO {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private Role role;
    private Status status;
    private Date created;

    public static UserResponseDTO fromUser(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getStatus(),
                user.getCreated());
        return userResponseDTO;
    }
}