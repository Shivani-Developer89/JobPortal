package com.JobPortal.JPP.dto.request;

import com.JobPortal.JPP.dto.common.EducationDTO;
import com.JobPortal.JPP.dto.common.ExperienceDTO;
import lombok.Data;

import java.util.List;

@Data
public class CandidateProfileRequestDTO {

    private String phone;

    private String location;

    private List<EducationDTO> education;



    private List<ExperienceDTO> experience;
    private String resumePath;
    private String skills;

    private String github;

    private String linkedin;
    private String leetcode;
}
