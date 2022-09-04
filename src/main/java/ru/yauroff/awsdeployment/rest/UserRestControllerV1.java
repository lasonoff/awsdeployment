package ru.yauroff.awsdeployment.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yauroff.awsdeployment.dto.UserRequestDTO;
import ru.yauroff.awsdeployment.dto.UserResponseDTO;
import ru.yauroff.awsdeployment.model.User;
import ru.yauroff.awsdeployment.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestControllerV1 {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> userList = userService.getAll();
        if (userList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<UserResponseDTO> listUserDTO = userList.stream()
                                                    .map(user -> UserResponseDTO.fromUser(user))
                                                    .collect(Collectors.toList());
        return new ResponseEntity<>(listUserDTO, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Validated UserRequestDTO userDTO) {
        HttpHeaders headers = new HttpHeaders();
        if (userDTO == null || userDTO.getLogin() == null || userDTO.getPassword() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userDTO.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userService.create(user);
        UserResponseDTO userResponseDTO = UserResponseDTO.fromUser(user);
        return new ResponseEntity<>(userResponseDTO, headers, HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody @Validated UserRequestDTO userDTO) {
        HttpHeaders headers = new HttpHeaders();
        if (userDTO == null || userDTO.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getById(userDTO.getId());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userDTO.updateUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = this.userService.update(user);
        UserResponseDTO userResponseDTO = UserResponseDTO.fromUser(user);
        return new ResponseEntity<>(userResponseDTO, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable("id") Long userId) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserResponseDTO userResponseDTO = UserResponseDTO.fromUser(user);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable("id") Long userId) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<Long> getCountUsers() {
        Long count = this.userService.getCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

}
