package com.JobPortal.JPP.dto.common;

import lombok.Data;

@Data
public class ExperienceDTO {

    private Long id;

    private String company;

    private String jobTitle;

    private String employmentType;

    private String location;

    private String yearsOfExperience;

    private String responsibilities;

    private String achievements;
}
