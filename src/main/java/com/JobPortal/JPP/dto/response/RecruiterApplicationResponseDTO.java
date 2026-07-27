package com.JobPortal.JPP.dto.response;

import com.JobPortal.JPP.entity.enums.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecruiterApplicationResponseDTO {
    private Long applicationId;
    private Long jobId;
    private String jobTitle;

    private String candidateName;

    private String candidateEmail;


    private ApplicationStatus status;

    private LocalDateTime appliedAt;
    private String resumeUrl;


}
