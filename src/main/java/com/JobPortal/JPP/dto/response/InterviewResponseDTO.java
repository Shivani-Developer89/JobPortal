package com.JobPortal.JPP.dto.response;

import com.JobPortal.JPP.entity.enums.InterviewResponse;
import com.JobPortal.JPP.entity.enums.InterviewStatus;
import com.JobPortal.JPP.entity.enums.InterviewType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewResponseDTO {

    private Long id;
    private Long applicationId;
    private Long jobId;
    private String jobTitle;

    private Long candidateId;
    private String candidateName;

    private Long recruiterId;
    private String recruiterName;

    private LocalDateTime scheduledAt;
    private Integer durationMinutes;
    private InterviewType type;
    private InterviewStatus status;
    private InterviewResponse candidateResponse;

    private String meetingLink;
    private String location;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
