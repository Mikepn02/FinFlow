package com.mikepn.banking.controller;

import com.mikepn.banking.domains.ApiResponse;
import com.mikepn.banking.dtos.auth.AuthenticationRequest;
import com.mikepn.banking.dtos.auth.AuthenticationResponse;
import com.mikepn.banking.dtos.auth.RegisterRequest;
import com.mikepn.banking.model.User;
import com.mikepn.banking.security.ValidPassword;
import com.mikepn.banking.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<String>> register(
            @RequestBody @Valid RegisterRequest request
    ) throws MessagingException {
        service.register(request);
        ApiResponse<String> response = new ApiResponse<>(
                null,
                "Registration successful",
                HttpStatus.ACCEPTED
        );
        return response.toResponseEntity();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        AuthenticationResponse response = service.authenticate(request);
        return new ApiResponse<>(
                response,
                "Authentication successful",
                HttpStatus.OK
        ).toResponseEntity();
    }


    @GetMapping("activate-account")
    public ResponseEntity<ApiResponse<String>> confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
        ApiResponse<String> response = new ApiResponse<>(
                null,
                "Account activated successfully",
                HttpStatus.OK
        );
        return response.toResponseEntity();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestParam("email") String email) throws MessagingException {
        String response = service.forgotPassword(email);
        return new ApiResponse<>(
                response,
                "Password reset link sent",
                HttpStatus.OK
        ).toResponseEntity();

    }


    @PutMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestParam("token") String token, @RequestParam("password") String password) {
        String response = service.resetPassword(token, password);
        return new ApiResponse<>(
                response,
                "Password Reset Successfully",
                HttpStatus.OK
        ).toResponseEntity();
    }

    @PutMapping("/update-password")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            @RequestParam("email") String email,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword
    ) {
        String response = service.updatePassword(email, currentPassword, newPassword);
        return new ApiResponse<>(
                response,
                "Password updated successfully",
                HttpStatus.OK
        ).toResponseEntity();
    }

    /*

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getAuthenticatedUser() {
       User loggedInUser = service.getLoggedInUser();
        return new ApiResponse<>(
                loggedInUser,
                "User details fetched successfully",
                HttpStatus.OK
        ).toResponseEntity();
    }

     */
}
