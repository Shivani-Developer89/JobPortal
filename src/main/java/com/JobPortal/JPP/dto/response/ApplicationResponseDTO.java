package com.JobPortal.JPP.dto.response;


import com.JobPortal.JPP.entity.enums.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationResponseDTO {

        private Long id;

        private Long candidateId;

        private Long jobId;
        private String jobTitle;

        private ApplicationStatus status;

        private LocalDateTime appliedAt;
    }
