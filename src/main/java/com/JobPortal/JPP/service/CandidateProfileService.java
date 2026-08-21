package com.JobPortal.JPP.service;


import com.JobPortal.JPP.dto.request.CandidateProfileRequestDTO;
import com.JobPortal.JPP.dto.response.CandidateProfileResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface CandidateProfileService {

    CandidateProfileResponseDTO createOrUpdateProfile(
            CandidateProfileRequestDTO request);
    CandidateProfileResponseDTO uploadProfileImage(MultipartFile image);

    CandidateProfileResponseDTO getMyProfile();

    CandidateProfileResponseDTO getCandidateProfile(Long candidateId);
    Resource getProfileImage(Long candidateId);

}
