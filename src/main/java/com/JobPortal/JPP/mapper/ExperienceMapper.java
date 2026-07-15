package com.JobPortal.JPP.mapper;

import com.JobPortal.JPP.dto.request.ExperienceDTO;
import com.JobPortal.JPP.entity.Experience;
import org.springframework.stereotype.Component;

@Component
public class ExperienceMapper {

    public Experience toEntity(ExperienceDTO dto) {

        Experience experience = new Experience();

        experience.setCompany(dto.getCompany());
        experience.setJobTitle(dto.getJobTitle());
        experience.setEmploymentType(dto.getEmploymentType());
        experience.setLocation(dto.getLocation());
        experience.setYearsOfExperience(dto.getYearsOfExperience());
        experience.setResponsibilities(dto.getResponsibilities());
        experience.setAchievements(dto.getAchievements());

        return experience;
    }

    public ExperienceDTO toDTO(Experience experience) {

        ExperienceDTO dto = new ExperienceDTO();

        dto.setId(experience.getId());
        dto.setCompany(experience.getCompany());
        dto.setJobTitle(experience.getJobTitle());
        dto.setEmploymentType(experience.getEmploymentType());
        dto.setLocation(experience.getLocation());
        dto.setYearsOfExperience(experience.getYearsOfExperience());
        dto.setResponsibilities(experience.getResponsibilities());
        dto.setAchievements(experience.getAchievements());

        return dto;
    }
}
