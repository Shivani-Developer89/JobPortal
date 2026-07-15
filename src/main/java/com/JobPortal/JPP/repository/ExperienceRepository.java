package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.entity.CandidateProfile;
import com.JobPortal.JPP.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository
        extends JpaRepository<Experience, Long> {

    void deleteByCandidateProfile(CandidateProfile profile);
}
