package com.JobPortal.JPP.dto.response;

import lombok.Data;

@Data
public class RecruiterDashboardResponseDTO {
    private Long totalJobs;
    private Long totalApplications;
    private Long shortlisted;
    private Long hired;
    private Long rejected;

}
