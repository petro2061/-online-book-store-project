package project.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.onlinebookstore.dto.user.UserLoginRequestDto;
import project.onlinebookstore.dto.user.UserLoginResponseDto;
import project.onlinebookstore.dto.user.UserRegistrationRequestDto;
import project.onlinebookstore.dto.user.UserResponseDto;
import project.onlinebookstore.exception.RegistrationException;
import project.onlinebookstore.security.AuthenticationService;
import project.onlinebookstore.service.user.UserService;

@Tag(name = "Authentication and authorization",
        description = "Contains endpoints for authentication and authorization users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Registration new users",
            description = "Allows registration new user")
    @PostMapping("/registration")
    public UserResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException {
        return userService.register(registrationRequestDto);
    }

    @Operation(summary = "Login users",
            description = "Allows allows logging in registered users")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto loginRequestDto) {
        return authenticationService.authenticate(loginRequestDto);
    }
}
