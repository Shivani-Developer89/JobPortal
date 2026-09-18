package com.JobPortal.JPP.service;

import com.JobPortal.JPP.entity.PasswordResetToken;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.PasswordResetTokenRepository;
import com.JobPortal.JPP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl
        implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    @Override
    @Transactional
    public void forgotPassword(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist(
                                "No account found with this email"
                        )
                );


        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByUser(user)
                        .orElseGet(() -> {
                            PasswordResetToken newToken =
                                    new PasswordResetToken();
                            newToken.setUser(user);
                            return newToken;
                        });

        resetToken.setToken(token);
        resetToken.setExpiryDate(
                LocalDateTime.now().plusMinutes(30)
        );
        resetToken.setUsed(false);

        passwordResetTokenRepository.save(resetToken);


        // Frontend reset page
        String resetLink =
                "http://localhost:5173/reset-password?token="
                        + token;


        String emailBody =
                "Hello " + user.getName() + ",\n\n" +

                        "We received a request to reset your Job Portal password.\n\n" +

                        "Click the link below to create a new password:\n\n" +

                        resetLink + "\n\n" +

                        "This link will expire in 30 minutes.\n\n" +

                        "If you did not request a password reset, "
                        + "you can safely ignore this email.\n\n" +

                        "Regards,\n" +
                        "Job Portal Team";


        emailService.sendEmail(
                user.getEmail(),
                "Reset Your Job Portal Password",
                emailBody
        );
    }


    // =========================================================
    // RESET PASSWORD
    // =========================================================

    @Override
    @Transactional
    public void resetPassword(
            String token,
            String newPassword) {


        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid password reset token"
                                )
                        );


        // Check whether token was already used
        if (resetToken.isUsed()) {

            throw new IllegalArgumentException(
                    "This password reset link has already been used"
            );
        }


        // Check expiration
        if (resetToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "This password reset link has expired"
            );
        }


        if (newPassword == null ||
                newPassword.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Password cannot be empty"
            );
        }


        if (newPassword.length() < 8) {

            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters"
            );
        }


        User user =
                resetToken.getUser();


        // Encode password using the same encoder
        // used during registration/login
        user.setPassword(
                passwordEncoder.encode(newPassword)
        );


        userRepository.save(user);


        // Token cannot be reused
        resetToken.setUsed(true);

        passwordResetTokenRepository
                .save(resetToken);
    }
}