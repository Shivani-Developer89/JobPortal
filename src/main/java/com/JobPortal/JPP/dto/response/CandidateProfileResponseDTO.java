package com.JobPortal.JPP.dto.response;


import com.JobPortal.JPP.dto.request.EducationDTO;
import com.JobPortal.JPP.dto.request.ExperienceDTO;
import lombok.Data;

import java.util.List;

@Data
public class CandidateProfileResponseDTO {

    // From User
    private Long candidateId;
    private String name;
    private String email;

    // From CandidateProfile
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
