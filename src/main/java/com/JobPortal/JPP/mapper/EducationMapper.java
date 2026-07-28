package com.JobPortal.JPP.mapper;

import com.JobPortal.JPP.dto.common.EducationDTO;
import com.JobPortal.JPP.entity.Education;
import org.springframework.stereotype.Component;

@Component
public class EducationMapper {

    public Education toEntity(EducationDTO dto) {

        Education education = new Education();

        education.setLevel(dto.getLevel());
        education.setSchool(dto.getSchool());
        education.setBoard(dto.getBoard());
        education.setStream(dto.getStream());
        education.setDegree(dto.getDegree());
        education.setBranch(dto.getBranch());
        education.setCollege(dto.getCollege());
        education.setUniversity(dto.getUniversity());
        education.setPassingYear(dto.getPassingYear());
        education.setGradingType(dto.getGradingType());
        education.setScore(dto.getScore());

        return education;
    }

    public EducationDTO toDTO(Education education) {

        EducationDTO dto = new EducationDTO();

        dto.setId(education.getId());

        dto.setLevel(education.getLevel());

        dto.setSchool(education.getSchool());

        dto.setBoard(education.getBoard());
        dto.setCollege(education.getCollege());

        dto.setDegree(education.getDegree());

        dto.setBranch(education.getBranch());

        dto.setStream(education.getStream());

        dto.setUniversity(education.getUniversity());

        dto.setPassingYear(education.getPassingYear());

        dto.setGradingType(education.getGradingType());

        dto.setScore(education.getScore());

        return dto;
    }
}
