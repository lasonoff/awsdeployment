package ru.yauroff.awsdeployment.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.yauroff.awsdeployment.dto.AuthenticationRequestDTO;
import ru.yauroff.awsdeployment.model.User;
import ru.yauroff.awsdeployment.security.jwt.JwtTokenProvider;
import ru.yauroff.awsdeployment.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {
        try {
            User user = userService.findByLoginOrEmail(request.getIdentifier());
            if (user == null) {
                throw new UsernameNotFoundException("User doesn't exists!");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), request.getPassword()));
            String token = jwtTokenProvider.createToken(user.getLogin(), user.getRole()
                                                                             .name());
            Map<Object, Object> response = new HashMap<>();
            response.put("login", user.getLogin());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid identifier or password", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("permitAll()")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}
