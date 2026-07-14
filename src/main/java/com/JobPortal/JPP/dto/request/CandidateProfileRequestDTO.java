package com.JobPortal.JPP.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CandidateProfileRequestDTO {

    private String phone;

    private String location;

    private List<EducationDTO> education;

    private String skills;

    private String experience;

    private String github;

    private String linkedin;
    private String leetcode;
}
