package com.JobPortal.JPP.controller;

import com.JobPortal.JPP.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;

@GetMapping("/test-email")
public String testEmail() {
  
    emailService.sendEmail(
            "your_email@gmail.com",
            "Test Email",
            "Spring Boot email is working");

    return "Email sent";
}
