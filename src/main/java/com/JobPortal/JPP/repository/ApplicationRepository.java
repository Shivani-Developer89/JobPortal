package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.entity.Application;
import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ApplicationRepository extends JpaRepository<Application , Long> {
    List<Application> findByCandidate(User candidate);
    List<Application> findByJob(Job job);
    boolean existsByCandidateAndJob(User candidate, Job job);
    Long countByJobRecruiter(User recruiter);


}
