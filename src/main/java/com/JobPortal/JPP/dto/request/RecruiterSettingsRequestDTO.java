package com.JobPortal.JPP.dto.request;

import lombok.Data;

@Data
public class RecruiterSettingsRequestDTO {

    private boolean newApplicants;

    private boolean emailNotifications;

    private boolean jobExpiryReminders;

    private String defaultLocation;

    private String employmentType;

    private String salaryVisibility;
}