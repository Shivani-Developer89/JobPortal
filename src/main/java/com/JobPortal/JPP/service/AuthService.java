package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.LoginInputDTO;
import com.JobPortal.JPP.dto.request.RegisterInputDTO;
import com.JobPortal.JPP.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterInputDTO dto);

    AuthResponse login(LoginInputDTO dto);
}
