package com.JobPortal.JPP.repository;

import com.JobPortal.JPP.entity.Job;
import com.JobPortal.JPP.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    List<Job> id(Long id);
    List<Job> findByTitleContainingIgnoreCase(String title);
    Page<Job> findAll(Pageable pageable);
}
