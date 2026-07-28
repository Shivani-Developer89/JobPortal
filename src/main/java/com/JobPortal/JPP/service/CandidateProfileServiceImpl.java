package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.CandidateProfileRequestDTO;
import com.JobPortal.JPP.dto.common.EducationDTO;
import com.JobPortal.JPP.dto.common.ExperienceDTO;
import com.JobPortal.JPP.dto.response.CandidateProfileResponseDTO;
import com.JobPortal.JPP.entity.CandidateProfile;
import com.JobPortal.JPP.entity.Education;
import com.JobPortal.JPP.entity.Experience;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.Role;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.mapper.EducationMapper;
import com.JobPortal.JPP.mapper.ExperienceMapper;
import com.JobPortal.JPP.repository.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateProfileServiceImpl
        implements CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;
    private final ExperienceRepository experienceRepository;
    private final ExperienceMapper experienceMapper;


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

        profile.setSkills(request.getSkills());

        profile.setGithub(request.getGithub());
        profile.setLinkedin(request.getLinkedin());
        profile.setLeetcode(request.getLeetcode());

        profile.getEducation().clear();

        for (EducationDTO dto : request.getEducation()) {

            Education education = educationMapper.toEntity(dto);

            education.setCandidateProfile(profile);

            profile.getEducation().add(education);
        }


        profile.getExperience().clear();

        for (ExperienceDTO dto : request.getExperience()) {

            Experience experience = experienceMapper.toEntity(dto);

            experience.setCandidateProfile(profile);

            profile.getExperience().add(experience);
        }

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

        CandidateProfileResponseDTO dto = new CandidateProfileResponseDTO();

        dto.setCandidateId(candidate.getId());
        dto.setName(candidate.getName());
        dto.setEmail(candidate.getEmail());
        candidateProfileRepository.findByCandidate(candidate)
                .ifPresent(profile -> {

                    dto.setPhone(profile.getPhone());
                    dto.setLocation(profile.getLocation());

                    dto.setEducation(
                            profile.getEducation()
                                    .stream()
                                    .map(educationMapper::toDTO)
                                    .toList()
                    );

                    dto.setExperience(
                            profile.getExperience()
                                    .stream()
                                    .map(experienceMapper::toDTO)
                                    .toList()
                    );
                    dto.setResumePath(candidate.getResumePath());

                    dto.setSkills(profile.getSkills());
                    dto.setGithub(profile.getGithub());
                    dto.setLinkedin(profile.getLinkedin());
                    dto.setLeetcode(profile.getLeetcode());

                });

        return dto;
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
        dto.setEducation(
                profile.getEducation()
                        .stream()
                        .map(educationMapper::toDTO)
                        .toList()
        );
        dto.setSkills(profile.getSkills());
        dto.setExperience(
                profile.getExperience()
                        .stream()
                        .map(experienceMapper::toDTO)
                        .toList()
        );
        dto.setGithub(profile.getGithub());
        dto.setLinkedin(profile.getLinkedin());
        dto.setLeetcode(profile.getLeetcode());

        dto.setResumePath(candidate.getResumePath());
        dto.setResumeUploadedAt(candidate.getResumeUploadedAt());

        return dto;
    }
}