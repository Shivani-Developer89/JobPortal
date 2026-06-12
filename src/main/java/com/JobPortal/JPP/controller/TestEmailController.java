package com.JobPortal.JPP.controller;

import com.JobPortal.JPP.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestEmailController {

    private final EmailService emailService;

    @GetMapping("/test-email")
    public String testEmail() {

        emailService.sendEmail(
                "your_email@gmail.com",
                "Test Email",
                "Spring Boot email is working"
        );

        return "Email sent";
    }
}