package com.mikepn.banking.services.implementations;

import com.mikepn.banking.dtos.auth.AuthenticationRequest;
import com.mikepn.banking.dtos.auth.AuthenticationResponse;
import com.mikepn.banking.dtos.auth.RegisterRequest;
import com.mikepn.banking.enums.template.EmailTemplate;
import com.mikepn.banking.enums.user.EUserRole;
import com.mikepn.banking.model.ResetToken;
import com.mikepn.banking.model.User;
import com.mikepn.banking.model.VerificationToken;
import com.mikepn.banking.repository.IResetTokenRepository;
import com.mikepn.banking.repository.ITokenRepository;
import com.mikepn.banking.repository.IUserRepository;
import com.mikepn.banking.security.JwtService;
import com.mikepn.banking.services.AuthenticationService;
import com.mikepn.banking.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final ITokenRepository tokenRepository;
    private final IResetTokenRepository resetTokenRepository;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Value("${application.security.mailing.frontend.activation-url}")
    private String activationUrl;
    @Value("${application.security.mailing.frontend.reset-url}")
    private String reseturl;

    @Override
    public void register(RegisterRequest request) throws MessagingException {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(EUserRole.CUSTOMER)
                .build();

        userRepository.save(user);
        sendValidationEmail(user);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
         var auth = authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
         );

         var claims = new HashMap<String, Object>();
         var user = ((User) auth.getPrincipal());
         claims.put("id", user.getId());
         var jwt = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .user(user)
                .build();
    }

    @Override
    public void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getName(),
                EmailTemplate.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    @Override
    public void sendResetTokenEmail(User user) throws MessagingException {
        var newToken = generateAndSaveResetToken(user);
        String resetToken = String.format("http://localhost:8080/api/v1/auth/reset-password?token=%s", newToken);

        emailService.sendEmail(
                user.getEmail(),
                user.getName(),
                EmailTemplate.RESET_PASSWORD,
                reseturl,
                resetToken,
                "Reset password"
        );

    }



    @Override
    public String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        var token = VerificationToken.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        tokenRepository.save(token);
        return generatedToken;
    }


    @Override
    public void activateAccount(String token) throws MessagingException {
        VerificationToken savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Token"));

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation Token has expired. A new token has been sent to the same email");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    @Override
    public String forgotPassword(String email) throws MessagingException {
        try {
            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            sendResetTokenEmail(user);
            return "Password reset token sent to email";
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Something went wrong");
        }
    }

    @Override
    public String resetPassword(String token, String password) {
        ResetToken savedToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Token"));

        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            throw new RuntimeException("Reset token has expired");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        resetTokenRepository.delete(savedToken);
        return "Password successfully reset";
    }

    @Override
    public String updatePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email , currentPassword)
        );

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password successfully updated.";
    }

    @Override
    public String generateAndSaveResetToken(User user) {
        String generatedToken = generateToken();
        var token = ResetToken.builder()
                .user(user)
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        resetTokenRepository.save(token);
        return generatedToken;
    }

    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No authenticated user found");
        }
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }



    private String generateToken() {
        return UUID.randomUUID().toString() + UUID.randomUUID().toString();
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }




}
