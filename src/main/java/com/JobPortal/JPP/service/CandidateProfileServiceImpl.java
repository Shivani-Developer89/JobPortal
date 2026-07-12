package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.CandidateProfileRequestDTO;
import com.JobPortal.JPP.dto.response.CandidateProfileResponseDTO;
import com.JobPortal.JPP.entity.CandidateProfile;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.Role;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.ApplicationRepository;
import com.JobPortal.JPP.repository.CandidateProfileRepository;
import com.JobPortal.JPP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateProfileServiceImpl
        implements CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public CandidateProfileResponseDTO createOrUpdateProfile(
            CandidateProfileRequestDTO request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        if (candidate.getRole() != Role.CANDIDATE) {
            throw new RuntimeException(
                    "Only candidates can update profile");
        }


        CandidateProfile profile = candidateProfileRepository
                .findByCandidate(candidate)
                .orElse(new CandidateProfile());

        profile.setCandidate(candidate);
        profile.setPhone(request.getPhone());
        profile.setLocation(request.getLocation());
        profile.setEducation(request.getEducation());
        profile.setSkills(request.getSkills());
        profile.setExperience(request.getExperience());
        profile.setGithub(request.getGithub());
        profile.setLinkedin(request.getLinkedin());
        profile.setLeetcode(request.getLeetcode());

        profile = candidateProfileRepository.save(profile);

        return convertToDTO(profile);
    }

    @Override
    public CandidateProfileResponseDTO getMyProfile() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        CandidateProfile profile = candidateProfileRepository
                .findByCandidate(candidate)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found"));

        return convertToDTO(profile);
    }

    @Override
    public CandidateProfileResponseDTO getCandidateProfile(Long candidateId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesNotExist("User not found"));

        if (recruiter.getRole() != Role.RECRUITER) {
            throw new RuntimeException(
                    "Only recruiters can view candidate profiles");
        }

        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() ->
                        new UserDoesNotExist("Candidate not found"));

        if (!applicationRepository.existsByCandidateAndJobRecruiter(candidate, recruiter)) {
            throw new RuntimeException(
                    "You are not authorized to view this candidate");
        }

        CandidateProfile profile = candidateProfileRepository
                .findByCandidate(candidate)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found"));

        return convertToDTO(profile);
    }

    private CandidateProfileResponseDTO convertToDTO(
            CandidateProfile profile) {

        User candidate = profile.getCandidate();

        CandidateProfileResponseDTO dto =
                new CandidateProfileResponseDTO();

        dto.setCandidateId(candidate.getId());
        dto.setName(candidate.getName());
        dto.setEmail(candidate.getEmail());

        dto.setPhone(profile.getPhone());
        dto.setLocation(profile.getLocation());
        dto.setEducation(profile.getEducation());
        dto.setSkills(profile.getSkills());
        dto.setExperience(profile.getExperience());
        dto.setGithub(profile.getGithub());
        dto.setLinkedin(profile.getLinkedin());
        dto.setLeetcode(profile.getLeetcode());

        return dto;
    }
}