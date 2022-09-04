package ru.yauroff.awsdeployment.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDTO {
    private String identifier;
    private String password;
}