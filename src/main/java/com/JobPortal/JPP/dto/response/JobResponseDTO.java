package com.JobPortal.JPP.dto.response;

import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.*;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Data
@JsonPropertyOrder({
        "id",
        "companyName",
        "companyLogo",
        "title",
        "description",
        "location",
        "minSalary",
        "maxSalary",
        "recruiterName",
        "recruiterId",
        "jobType",
        "workMode",
        "experienceLevel",
       "minExperience",
        "maxExperience",
        "skills",
        "vacancies",
        "applicationDeadline",
        "createdAt",
        "updatedAt"
})
public class JobResponseDTO {
    private Long id;
    private String companyName;
    private String companyLogo;

    private String title;
    private String description;
    private String location;

    private Double minSalary;
    private Double maxSalary;
    private String recruiterName;
    private Long recruiterId;

    private JobType jobType;
    private WorkMode workMode;

    private ExperienceLevel experienceLevel;
    private Integer minExperience;
    private Integer maxExperience;

    private List<String> skills;

    private Integer vacancies;

    private LocalDate applicationDeadline;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean applied;
    private JobStatus status;


}