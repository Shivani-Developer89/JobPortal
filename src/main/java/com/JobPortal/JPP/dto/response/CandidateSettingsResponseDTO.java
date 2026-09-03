package com.JobPortal.JPP.dto.response;

import lombok.Data;

@Data
public class CandidateSettingsResponseDTO {

    private Long id;

    private boolean jobAlerts;

    private boolean emailNotifications;

    private boolean applicationUpdates;

    private String preferredLocation;

    private String employmentType;

    private String workMode;
}