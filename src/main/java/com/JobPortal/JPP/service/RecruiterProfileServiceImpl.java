package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.RecruiterProfileRequestDTO;
import com.JobPortal.JPP.dto.response.RecruiterProfileResponseDTO;
import com.JobPortal.JPP.entity.RecruiterProfile;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.repository.RecruiterProfileRepository;
import com.JobPortal.JPP.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

        /*
         * We will connect this to the same image-storage approach
         * already used by CandidateProfileServiceImpl.
         *
         * Do not implement a second file-storage mechanism.
         */

        throw new UnsupportedOperationException(
                "Profile image upload is not implemented yet."
        );
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