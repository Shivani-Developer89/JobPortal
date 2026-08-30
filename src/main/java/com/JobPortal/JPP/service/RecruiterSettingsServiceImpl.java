package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.RecruiterSettingsRequestDTO;
import com.JobPortal.JPP.dto.response.RecruiterSettingsResponseDTO;
import com.JobPortal.JPP.entity.RecruiterSettings;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.Role;
import com.JobPortal.JPP.exceptions.UserDoesNotExist;
import com.JobPortal.JPP.repository.RecruiterSettingsRepository;
import com.JobPortal.JPP.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RecruiterSettingsServiceImpl
        implements RecruiterSettingsService {

    private final RecruiterSettingsRepository settingsRepository;
    private final UserRepository userRepository;

    public RecruiterSettingsServiceImpl(
            RecruiterSettingsRepository settingsRepository,
            UserRepository userRepository) {

        this.settingsRepository = settingsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RecruiterSettingsResponseDTO getMySettings() {

        User recruiter = getAuthenticatedRecruiter();

        RecruiterSettings settings =
                settingsRepository
                        .findByRecruiter(recruiter)
                        .orElseGet(() -> createDefaultSettings(recruiter));

        return convertToDTO(settings);
    }

    @Override
    public RecruiterSettingsResponseDTO updateMySettings(
            RecruiterSettingsRequestDTO request) {

        User recruiter = getAuthenticatedRecruiter();

        RecruiterSettings settings =
                settingsRepository
                        .findByRecruiter(recruiter)
                        .orElseGet(() -> createDefaultSettings(recruiter));

        settings.setNewApplicants(
                request.isNewApplicants()
        );

        settings.setEmailNotifications(
                request.isEmailNotifications()
        );

        settings.setJobExpiryReminders(
                request.isJobExpiryReminders()
        );

        settings.setDefaultLocation(
                clean(request.getDefaultLocation())
        );

        settings.setEmploymentType(
                clean(request.getEmploymentType())
        );

        settings.setSalaryVisibility(
                clean(request.getSalaryVisibility())
        );

        settings = settingsRepository.save(settings);

        return convertToDTO(settings);
    }

    private User getAuthenticatedRecruiter() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new UserDoesNotExist(
                                        "User not found"
                                ));

        if (user.getRole() != Role.RECRUITER) {
            throw new AccessDeniedException(
                    "Only recruiters can access recruiter settings"
            );
        }

        return user;
    }

    private RecruiterSettings createDefaultSettings(
            User recruiter) {

        RecruiterSettings settings =
                new RecruiterSettings();

        settings.setRecruiter(recruiter);

        settings.setNewApplicants(true);
        settings.setEmailNotifications(true);
        settings.setJobExpiryReminders(true);

        settings.setDefaultLocation("");
        settings.setEmploymentType("Full Time");
        settings.setSalaryVisibility("Disclose");

        return settingsRepository.save(settings);
    }

    private RecruiterSettingsResponseDTO convertToDTO(
            RecruiterSettings settings) {

        RecruiterSettingsResponseDTO dto =
                new RecruiterSettingsResponseDTO();

        dto.setId(settings.getId());

        dto.setNewApplicants(
                settings.isNewApplicants()
        );

        dto.setEmailNotifications(
                settings.isEmailNotifications()
        );

        dto.setJobExpiryReminders(
                settings.isJobExpiryReminders()
        );

        dto.setDefaultLocation(
                settings.getDefaultLocation()
        );

        dto.setEmploymentType(
                settings.getEmploymentType()
        );

        dto.setSalaryVisibility(
                settings.getSalaryVisibility()
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