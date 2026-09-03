package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.CandidateSettingsRequestDTO;
import com.JobPortal.JPP.dto.response.CandidateSettingsResponseDTO;

public interface CandidateSettingsService {

    CandidateSettingsResponseDTO getMySettings();

    CandidateSettingsResponseDTO updateMySettings(
            CandidateSettingsRequestDTO request
    );
}