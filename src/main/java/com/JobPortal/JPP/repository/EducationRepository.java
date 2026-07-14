package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.entity.CandidateProfile;
import com.JobPortal.JPP.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EducationRepository
        extends JpaRepository<Education, Long> {
    @Transactional
    @Modifying
    void deleteByCandidateProfile(CandidateProfile candidateProfile);
}
