package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByTitleContainingIgnoreCase(String title);

    Page<Job> findByStatus(
            JobStatus status,
            Pageable pageable
    );

    Long countByRecruiter(User recruiter);

    List<Job> findByRecruiter(User recruiter);
}