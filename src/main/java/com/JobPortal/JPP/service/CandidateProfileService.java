package com.JobPortal.JPP.service;


import com.JobPortal.JPP.dto.request.CandidateProfileRequestDTO;
import com.JobPortal.JPP.dto.response.CandidateProfileResponseDTO;

public interface CandidateProfileService {

    CandidateProfileResponseDTO createOrUpdateProfile(
            CandidateProfileRequestDTO request);

    CandidateProfileResponseDTO getMyProfile();

    CandidateProfileResponseDTO getCandidateProfile(Long candidateId);

}
