package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.RecruiterProfileRequestDTO;
import com.JobPortal.JPP.dto.response.RecruiterProfileResponseDTO;
import com.JobPortal.JPP.entity.RecruiterProfile;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.Role;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.RecruiterProfileRepository;
import com.JobPortal.JPP.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class RecruiterProfileServiceImpl
        implements RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;

    @Override
    public RecruiterProfileResponseDTO createOrUpdateProfile(
            RecruiterProfileRequestDTO request) {

        User recruiter = getAuthenticatedRecruiter();

        RecruiterProfile profile =
                recruiterProfileRepository
                        .findByRecruiter(recruiter)
                        .orElseGet(() -> {
                            RecruiterProfile newProfile =
                                    new RecruiterProfile();

                            newProfile.setRecruiter(recruiter);

                            return newProfile;
                        });

        profile.setPhone(request.getPhone());
        profile.setDesignation(request.getDesignation());
        profile.setCompanyName(request.getCompanyName());
        profile.setCompanyWebsite(request.getCompanyWebsite());
        profile.setCompanyLocation(request.getCompanyLocation());
        profile.setCompanyDescription(
                request.getCompanyDescription()
        );

        RecruiterProfile savedProfile =
                recruiterProfileRepository.save(profile);

        return convertToDTO(savedProfile);
    }

    @Override
    public RecruiterProfileResponseDTO getMyProfile() {

        User recruiter = getAuthenticatedRecruiter();

        RecruiterProfile profile =
                recruiterProfileRepository
                        .findByRecruiter(recruiter)
                        .orElseGet(() -> {
                            RecruiterProfile newProfile =
                                    new RecruiterProfile();

                            newProfile.setRecruiter(recruiter);

                            return newProfile;
                        });

        return convertToDTO(profile);
    }

    @Override
    public RecruiterProfileResponseDTO uploadProfileImage(
            MultipartFile image) {

        try {

            if (image == null || image.isEmpty()) {
                throw new RuntimeException(
                        "Profile image is required"
                );
            }

            // Get currently logged-in recruiter
            String email = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            User recruiter = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new UserDoesNotExist("User not found")
                    );

            if (recruiter.getRole() != Role.RECRUITER) {
                throw new RuntimeException(
                        "Only recruiters can upload profile image"
                );
            }

            RecruiterProfile profile =
                    recruiterProfileRepository
                            .findByRecruiter(recruiter)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Recruiter profile not found"
                                    )
                            );

            // Recruiter-specific directory
            Path uploadDir = Paths.get(
                    "uploads",
                    "profile-images",
                    "recruiter",
                    String.valueOf(recruiter.getId())
            );

            Files.createDirectories(uploadDir);

            // Get file extension
            String originalName =
                    image.getOriginalFilename();

            String extension = "";

            if (originalName != null &&
                    originalName.contains(".")) {

                extension = originalName.substring(
                        originalName.lastIndexOf(".")
                );
            }

            // Unique filename
            String fileName =
                    "profile_" +
                            System.currentTimeMillis() +
                            extension;

            Path filePath =
                    uploadDir.resolve(fileName);

            // Save image
            Files.copy(
                    image.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // Save path in recruiter profile
            profile.setProfileImagePath(
                    filePath.toString()
            );

            recruiterProfileRepository.save(profile);

            return convertToDTO(profile);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload profile image",
                    e
            );
        }
    }

    private User getAuthenticatedRecruiter() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated recruiter not found"
                        )
                );
    }

    private RecruiterProfileResponseDTO convertToDTO(
            RecruiterProfile profile) {

        User recruiter = profile.getRecruiter();

        RecruiterProfileResponseDTO dto =
                new RecruiterProfileResponseDTO();

        dto.setRecruiterId(recruiter.getId());
        dto.setName(recruiter.getName());
        dto.setEmail(recruiter.getEmail());
        dto.setRole(
                recruiter.getRole() != null
                        ? recruiter.getRole().name()
                        : null
        );

        dto.setPhone(profile.getPhone());
        dto.setDesignation(profile.getDesignation());
        dto.setCompanyName(profile.getCompanyName());
        dto.setCompanyWebsite(profile.getCompanyWebsite());
        dto.setCompanyLocation(profile.getCompanyLocation());
        dto.setCompanyDescription(
                profile.getCompanyDescription()
        );
        dto.setProfileImagePath(
                profile.getProfileImagePath()
        );

        return dto;
    }
}