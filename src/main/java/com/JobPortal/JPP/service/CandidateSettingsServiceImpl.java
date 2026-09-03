package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.CandidateSettingsRequestDTO;
import com.JobPortal.JPP.dto.response.CandidateSettingsResponseDTO;
import com.JobPortal.JPP.entity.CandidateSettings;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.Role;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.CandidateSettingsRepository;
import com.JobPortal.JPP.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CandidateSettingsServiceImpl
        implements CandidateSettingsService {

    private final CandidateSettingsRepository settingsRepository;
    private final UserRepository userRepository;

    public CandidateSettingsServiceImpl(
            CandidateSettingsRepository settingsRepository,
            UserRepository userRepository) {

        this.settingsRepository = settingsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CandidateSettingsResponseDTO getMySettings() {

        User candidate = getAuthenticatedCandidate();

        CandidateSettings settings =
                settingsRepository
                        .findByCandidate(candidate)
                        .orElseGet(() ->
                                createDefaultSettings(candidate)
                        );

        return convertToDTO(settings);
    }

    @Override
    public CandidateSettingsResponseDTO updateMySettings(
            CandidateSettingsRequestDTO request) {

        User candidate = getAuthenticatedCandidate();

        CandidateSettings settings =
                settingsRepository
                        .findByCandidate(candidate)
                        .orElseGet(() ->
                                createDefaultSettings(candidate)
                        );

        settings.setJobAlerts(
                request.isJobAlerts()
        );

        settings.setEmailNotifications(
                request.isEmailNotifications()
        );

        settings.setApplicationUpdates(
                request.isApplicationUpdates()
        );

        settings.setPreferredLocation(
                clean(request.getPreferredLocation())
        );

        settings.setEmploymentType(
                clean(request.getEmploymentType())
        );

        settings.setWorkMode(
                clean(request.getWorkMode())
        );

        settings =
                settingsRepository.save(settings);

        return convertToDTO(settings);
    }

    // =========================
    // Authenticated Candidate
    // =========================

    private User getAuthenticatedCandidate() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User candidate =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new UserDoesNotExist(
                                        "User not found"
                                ));

        if (candidate.getRole() != Role.CANDIDATE) {

            throw new AccessDeniedException(
                    "Only candidates can access candidate settings"
            );
        }

        return candidate;
    }

    // =========================
    // Default Settings
    // =========================

    private CandidateSettings createDefaultSettings(
            User candidate) {

        CandidateSettings settings =
                new CandidateSettings();

        settings.setCandidate(candidate);

        settings.setJobAlerts(true);

        settings.setEmailNotifications(true);

        settings.setApplicationUpdates(true);

        settings.setPreferredLocation("");

        settings.setEmploymentType("Full Time");

        settings.setWorkMode("Any");

        return settingsRepository.save(settings);
    }

    // =========================
    // DTO Conversion
    // =========================

    private CandidateSettingsResponseDTO convertToDTO(
            CandidateSettings settings) {

        CandidateSettingsResponseDTO dto =
                new CandidateSettingsResponseDTO();

        dto.setId(settings.getId());

        dto.setJobAlerts(
                settings.isJobAlerts()
        );

        dto.setEmailNotifications(
                settings.isEmailNotifications()
        );

        dto.setApplicationUpdates(
                settings.isApplicationUpdates()
        );

        dto.setPreferredLocation(
                settings.getPreferredLocation()
        );

        dto.setEmploymentType(
                settings.getEmploymentType()
        );

        dto.setWorkMode(
                settings.getWorkMode()
        );

        return dto;
    }

    private String clean(String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }
}