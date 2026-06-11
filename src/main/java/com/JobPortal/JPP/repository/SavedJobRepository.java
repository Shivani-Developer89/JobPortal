package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.SavedJob;
import com.JobPortal.JPP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository
        extends JpaRepository<SavedJob, Long> {

    Optional<SavedJob> findByCandidateAndJob(
            User candidate,
            Job job);

    List<SavedJob> findByCandidate(
            User candidate);
}
