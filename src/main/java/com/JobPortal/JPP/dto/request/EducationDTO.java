package com.JobPortal.JPP.dto.request;

import lombok.Data;

@Data
public class EducationDTO {

    private Long id;

    private String level;

    private String school;

    private String board;

    private String stream;

    private String degree;

    private String branch;

    private String college;

    private String university;

    private Integer passingYear;

    private String gradingType;

    private String score;
}
