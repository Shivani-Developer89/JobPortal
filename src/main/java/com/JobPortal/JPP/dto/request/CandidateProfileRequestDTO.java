package com.JobPortal.JPP.dto.request;

import lombok.Data;

@Data
public class CandidateProfileRequestDTO {

    private String phone;

    private String location;

    private String education;

    private String skills;

    private String experience;

    private String github;

    private String linkedin;
    private String leetcode;
}
