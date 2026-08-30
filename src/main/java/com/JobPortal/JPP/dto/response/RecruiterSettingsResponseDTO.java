package com.JobPortal.JPP.dto.response;

import lombok.Data;

@Data
public class RecruiterSettingsResponseDTO {

    private Long id;

    private boolean newApplicants;

    private boolean emailNotifications;

    private boolean jobExpiryReminders;

    private String defaultLocation;

    private String employmentType;

    private String salaryVisibility;
}