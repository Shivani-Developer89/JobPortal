package com.JobPortal.JPP.dto.response;

import com.JobPortal.JPP.dto.common.ExperienceDTO;
import com.JobPortal.JPP.entity.enums.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RecruiterApplicationResponseDTO {
    private Long applicationId;
    private Long jobId;
    private String jobTitle;
    private Long candidateId;
    private String candidateName;

    private String candidateEmail;
    private String candidateLocation;
    private List<ExperienceDTO> candidateExperience;
    private String profileImagePath;


    private ApplicationStatus status;

    private LocalDateTime appliedAt;
    private String resumeUrl;
}
