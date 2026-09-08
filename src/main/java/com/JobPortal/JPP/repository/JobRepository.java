package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.entity.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @Query("""
    SELECT DISTINCT j
    FROM Job j
    LEFT JOIN j.skills s
    WHERE j.status = :status
    AND (
        :keyword IS NULL
        OR :keyword = ''
        OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(s) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
    AND (
        :location IS NULL
        OR :location = ''
        OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))
    )
    """)
    List<Job> searchJobs(
            @Param("keyword") String keyword,
            @Param("location") String location,
            @Param("status") JobStatus status
    );

    List<Job> findByTitleContainingIgnoreCase(String title);

    Page<Job> findByStatus(
            JobStatus status,
            Pageable pageable
    );

    Long countByRecruiter(User recruiter);

    List<Job> findByRecruiter(User recruiter);
}