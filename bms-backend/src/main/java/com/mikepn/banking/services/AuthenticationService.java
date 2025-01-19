package com.mikepn.banking.services;

import com.mikepn.banking.dtos.auth.AuthenticationRequest;
import com.mikepn.banking.dtos.auth.AuthenticationResponse;
import com.mikepn.banking.dtos.auth.RegisterRequest;
import com.mikepn.banking.model.User;
import jakarta.mail.MessagingException;


public interface AuthenticationService {

    void register(RegisterRequest request) throws MessagingException;

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void sendValidationEmail(User user) throws MessagingException;

    void sendResetTokenEmail(User user) throws MessagingException;

    String generateAndSaveActivationToken(User user);

    void activateAccount(String token) throws MessagingException;

    String forgotPassword(String email) throws MessagingException;

    String resetPassword(String token, String password);

    String updatePassword(String email, String currentPassword, String newPassword);

    String generateAndSaveResetToken(User user);

    User getLoggedInUser();
}
