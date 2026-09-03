package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.entity.CandidateSettings;
import com.JobPortal.JPP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateSettingsRepository
        extends JpaRepository<CandidateSettings, Long> {

    Optional<CandidateSettings> findByCandidate(User candidate);
}