package com.JobPortal.JPP.dto.response;


import com.JobPortal.JPP.dto.common.EducationDTO;
import com.JobPortal.JPP.dto.common.ExperienceDTO;
import lombok.Data;

import java.time.LocalDateTime;
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
    private LocalDateTime resumeUploadedAt;
    private String skills;
    private String github;
    private String linkedin;
    private String leetcode;

}
