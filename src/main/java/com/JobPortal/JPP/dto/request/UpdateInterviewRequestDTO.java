package com.JobPortal.JPP.dto.request;

import com.JobPortal.JPP.entity.enums.InterviewType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateInterviewRequestDTO {

    private LocalDateTime scheduledAt;
    private Integer durationMinutes;
    private InterviewType type;
    private String meetingLink;
    private String location;
    private String notes;
}
