package com.JobPortal.JPP.dto.request;

import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.security.jwt.JwtService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginInputDTO {
    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;


}
