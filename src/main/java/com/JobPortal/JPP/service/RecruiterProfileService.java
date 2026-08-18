package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.RecruiterProfileRequestDTO;
import com.JobPortal.JPP.dto.response.RecruiterProfileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface RecruiterProfileService {

    RecruiterProfileResponseDTO createOrUpdateProfile(
            RecruiterProfileRequestDTO request
    );

    RecruiterProfileResponseDTO getMyProfile();

    RecruiterProfileResponseDTO uploadProfileImage(
            MultipartFile image
    );
}