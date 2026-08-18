package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.entity.RecruiterProfile;
import com.JobPortal.JPP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruiterProfileRepository
        extends JpaRepository<RecruiterProfile, Long> {

    Optional<RecruiterProfile> findByRecruiter(User recruiter);
}