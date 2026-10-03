package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.entity.Application;
import com.JobPortal.JPP.entity.Interview;
import com.JobPortal.JPP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByApplication(Application application);

    @Query("""
        SELECT i
        FROM Interview i
        WHERE i.application.candidate.id = :candidateId
        ORDER BY i.scheduledAt ASC
    """)
    List<Interview> findByCandidateIdOrderByScheduledAtAsc(
            @Param("candidateId") Long candidateId
    );

    List<Interview> findByApplicationJobRecruiterOrderByScheduledAtAsc(
            User recruiter
    );
}