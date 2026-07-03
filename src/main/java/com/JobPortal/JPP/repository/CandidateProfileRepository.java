package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.entity.CandidateProfile;
import com.JobPortal.JPP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateProfileRepository
        extends JpaRepository<CandidateProfile, Long> {

    Optional<CandidateProfile> findByCandidate(User candidate);

}