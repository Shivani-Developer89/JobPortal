package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.entity.RecruiterSettings;
import com.JobPortal.JPP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruiterSettingsRepository
        extends JpaRepository<RecruiterSettings, Long> {

    Optional<RecruiterSettings> findByRecruiter(User recruiter);
}