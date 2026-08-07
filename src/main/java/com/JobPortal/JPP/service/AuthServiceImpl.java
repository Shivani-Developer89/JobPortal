package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.ForgetPasswordDTO;
import com.JobPortal.JPP.dto.request.LoginInputDTO;
import com.JobPortal.JPP.dto.request.RegisterInputDTO;
import com.JobPortal.JPP.dto.response.AuthResponse;
import com.JobPortal.JPP.dto.response.ResetPasswordDTO;
import com.JobPortal.JPP.entity.PasswordResetOtp;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.exceptions.InvalidCredentialsException;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.PasswordResetOtpRepository;
import com.JobPortal.JPP.repository.UserRepository;
import com.JobPortal.JPP.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements  AuthService{
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private  final PasswordResetOtpRepository passwordResetOtpRepository;
    private final EmailService emailService;


    @Override
    public AuthResponse register(RegisterInputDTO dto) {
        Optional<User> existingUser =
                userRepository.findByEmail(dto.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        user.setRole(dto.getRole());
        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail());


        return new AuthResponse(
                token,
                savedUser.getRole().name(),
                savedUser.getName(),
                "Registration Successful"
        );

    }

    @Override
    public AuthResponse login(LoginInputDTO dto) {


            User user = userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() ->
                            new UserDoesNotExist("User not found"));

            boolean isPasswordMatch =
                    passwordEncoder.matches(
                            dto.getPassword(),
                            user.getPassword()
                    );

            if (!isPasswordMatch) {
                throw new InvalidCredentialsException(
                        "Invalid email or password"
                );
            }
           String token =
                jwtService.generateToken(user.getEmail());
        System.out.println(
                jwtService.extractEmail(token)
        );

        return new AuthResponse(
                token,
                user.getRole().name(),
                user.getName(),
                "Login Successful"
        );
        }

    @Override
    public String forgetPassword(ForgetPasswordDTO request) {
        User user =userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserDoesNotExist("Email not registered"));
        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        PasswordResetOtp resetOtp = passwordResetOtpRepository.findByEmail(user.getEmail())
                .orElse(new PasswordResetOtp());
        resetOtp.setEmail(user.getEmail());
        resetOtp.setOtp(otp);
        resetOtp.setExpiryTime(LocalDateTime.now().plusMinutes(10));

        passwordResetOtpRepository.save(resetOtp);


        emailService.sendEmail(
                user.getEmail(),
                "Password reset OTP",
                "Your Otp is "+ otp);

        return "OTP sent Successfully";
    }

    @Override
    public String resetPassword(ResetPasswordDTO request) {
        PasswordResetOtp resetOtp = passwordResetOtpRepository.findByEmailAndOtp(request.getEmail(),
                request.getOtp()).orElseThrow(() -> new RuntimeException("Invalid Otp"));
        if(resetOtp.getExpiryTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("OTP Expired");
        }
        User user =userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->  new UserDoesNotExist("User not found"));
        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()));

        userRepository.save(user);

        emailService.sendEmail(
                user.getEmail(),
                "Password Changed Successfully",
                "Hello " + user.getName() + ",\n\n" +
                        "Your Job Portal account password was changed successfully.\n\n" +
                        "If you did not make this change, please contact support immediately.\n\n" +
                        "Regards,\n" +
                        "Job Portal Team"
        );

        passwordResetOtpRepository.delete(resetOtp);

        return "Password reset successfully";
    }
}

