package com.JobPortal.JPP.dto.request;

import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.ExperienceLevel;
import com.JobPortal.JPP.entity.enums.JobType;
import com.JobPortal.JPP.entity.enums.WorkMode;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data

public class JobRequestDTO {

    private String companyName;
    private String companyLogo;

    private String title;
    private String description;
    private String location;

    private Double minSalary;
    private Double maxSalary;

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

}
