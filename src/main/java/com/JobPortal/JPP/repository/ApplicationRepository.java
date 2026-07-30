package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.dto.response.CandidateProfileResponseDTO;
import com.JobPortal.JPP.entity.Application;
import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ApplicationRepository extends JpaRepository<Application , Long> {
    List<Application> findByCandidate(User candidate);
    List<Application> findByJob(Job job);
    boolean existsByCandidateAndJob(User candidate, Job job);
    Long countByCandidate(User candidate);

    Long countByCandidateAndStatus(
            User candidate,
            ApplicationStatus status);

    Long countByJobRecruiter(User recruiter);
    Long countByJobRecruiterAndStatus(
            User recruiter,
            ApplicationStatus status);
    List<Application> findTop10ByJobRecruiterOrderByAppliedAtDesc(User recruiter);
    boolean existsByCandidateAndJobRecruiter(User candidate, User recruiter);
    boolean existsByCandidateAndJobAndStatusIn(
            User candidate,
            Job job,
            List<ApplicationStatus> statuses
    );
    boolean existsByJob_Id(Long jobId);
}
