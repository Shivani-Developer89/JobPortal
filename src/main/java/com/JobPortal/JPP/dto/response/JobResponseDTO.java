package com.JobPortal.JPP.dto.response;

import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.ExperienceLevel;
import com.JobPortal.JPP.entity.enums.JobType;
import lombok.Data;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Data
@JsonPropertyOrder({
        "id",
        "title",
        "companyName",
        "recruiterName",
        "location",
        "salary",
        "jobType",
        "experienceLevel",
        "description",
        "createdAt"
})
public class JobResponseDTO {

    private Long id;

    private String title;

    private String description;

    private String location;

    private Double salary;

    private JobType jobType;

    private ExperienceLevel experienceLevel;

    // From recruiter
    private String recruiterName;
    private String companyName;

    private LocalDateTime createdAt;
}