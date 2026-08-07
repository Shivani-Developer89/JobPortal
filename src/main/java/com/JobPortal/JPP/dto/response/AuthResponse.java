package com.JobPortal.JPP.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String role;
    private String name;

    private String message;

}