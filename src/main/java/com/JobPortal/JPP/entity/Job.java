package com.JobPortal.JPP.entity;

import com.JobPortal.JPP.entity.enums.ExperienceLevel;
import com.JobPortal.JPP.entity.enums.JobType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    private String location;

    private Double salary;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    private LocalDateTime createdAt;
}
