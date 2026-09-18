package com.JobPortal.JPP.controller;

import com.JobPortal.JPP.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam String email) {

        passwordResetService.forgotPassword(email);

        return ResponseEntity.ok(
                "Password reset link has been sent to your email."
        );
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        passwordResetService.resetPassword(
                token,
                newPassword
        );

        return ResponseEntity.ok(
                "Password has been reset successfully."
        );
    }
}