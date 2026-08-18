package com.JobPortal.JPP.dto.response;

import lombok.Data;

@Data
public class RecruiterProfileResponseDTO {

    // From User
    private Long recruiterId;

    private String name;

    private String email;

    private String role;

    // From RecruiterProfile
    private String phone;

    private String designation;

    private String companyName;

    private String companyWebsite;

    private String companyLocation;

    private String companyDescription;

    private String profileImagePath;
}