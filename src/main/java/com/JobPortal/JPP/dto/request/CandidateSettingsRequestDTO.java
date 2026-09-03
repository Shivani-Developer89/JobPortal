package com.JobPortal.JPP.dto.request;

import lombok.Data;

@Data
public class CandidateSettingsRequestDTO {

    private boolean jobAlerts;

    private boolean emailNotifications;

    private boolean applicationUpdates;

    private String preferredLocation;

    private String employmentType;

    private String workMode;
}