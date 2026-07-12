package com.JobPortal.JPP.dto.response;


import lombok.Data;

@Data
public class CandidateProfileResponseDTO {

    // From User
    private Long candidateId;
    private String name;
    private String email;

    // From CandidateProfile
    private String phone;
    private String location;
    private String education;
    private String skills;
    private String experience;
    private String github;
    private String linkedin;
    private String leetcode;

}
