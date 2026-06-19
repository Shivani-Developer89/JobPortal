package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.ForgetPasswordDTO;
import com.JobPortal.JPP.dto.request.LoginInputDTO;
import com.JobPortal.JPP.dto.request.RegisterInputDTO;
import com.JobPortal.JPP.dto.response.AuthResponse;
import com.JobPortal.JPP.dto.response.ResetPasswordDTO;

public interface AuthService {
    AuthResponse register(RegisterInputDTO dto);

    AuthResponse login(LoginInputDTO dto);


    String forgetPassword(ForgetPasswordDTO request);
    String resetPassword(ResetPasswordDTO request);
}
