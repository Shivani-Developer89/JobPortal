package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.LoginInputDTO;
import com.JobPortal.JPP.dto.request.RegisterInputDTO;
import com.JobPortal.JPP.dto.response.AuthResponse;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.exceptions.InvalidCredentialsException;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements  AuthService{
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

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
        userRepository.save(user);

        return new AuthResponse(
                "Dummy Token",
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

            return new AuthResponse(
                    "Login Successful",
                    "Dummy Token"
            );
        }
    }


