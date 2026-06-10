package com.JobPortal.JPP.dto.response;

import lombok.Data;

@Data
public class CandidateDashboardResponseDTO {

    private Long totalApplications;
    private Long pendingApplications;
    private Long acceptedApplications;
    private Long rejectedApplications;
}
