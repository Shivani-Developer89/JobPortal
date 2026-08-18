package com.JobPortal.JPP.dto.request;

import lombok.Data;

@Data
public class RecruiterProfileRequestDTO {

    private String phone;

    private String designation;

    private String companyName;

    private String companyWebsite;

    private String companyLocation;

    private String companyDescription;
}