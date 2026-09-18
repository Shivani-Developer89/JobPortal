package com.JobPortal.JPP.service;

public interface PasswordResetService {

    void forgotPassword(String email);

    void resetPassword(String token, String newPassword);
}