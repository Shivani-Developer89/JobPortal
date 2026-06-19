package com.JobPortal.JPP.controller;

import com.JobPortal.JPP.dto.request.ForgetPasswordDTO;
import com.JobPortal.JPP.dto.request.LoginInputDTO;
import com.JobPortal.JPP.dto.request.RegisterInputDTO;
import com.JobPortal.JPP.dto.response.AuthResponse;
import com.JobPortal.JPP.dto.response.ResetPasswordDTO;
import com.JobPortal.JPP.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterInputDTO dto) {

        return ResponseEntity.ok(
                authService.register(dto)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginInputDTO dto) {

        return ResponseEntity.ok(
                authService.login(dto)
        );
    }
    @PostMapping("/forgot-password")
    public String forgotPassword(
            @RequestBody ForgetPasswordDTO request) {

        return authService.forgetPassword(request);
    }
    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestBody ResetPasswordDTO request) {

        return authService.resetPassword(request);
    }
}
