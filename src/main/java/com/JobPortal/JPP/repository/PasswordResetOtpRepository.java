package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp ,Long> {
    Optional<PasswordResetOtp> findByEmail(String email);
    Optional<PasswordResetOtp> findByEmailAndOtp(String email, String otp);
}
