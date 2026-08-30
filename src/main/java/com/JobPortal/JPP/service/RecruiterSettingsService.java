package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.RecruiterSettingsRequestDTO;
import com.JobPortal.JPP.dto.response.RecruiterSettingsResponseDTO;

public interface RecruiterSettingsService {

    RecruiterSettingsResponseDTO getMySettings();

    RecruiterSettingsResponseDTO updateMySettings(
            RecruiterSettingsRequestDTO request
    );
}