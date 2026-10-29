package com.JobPortal.JPP.entity;

import com.JobPortal.JPP.entity.enums.ExperienceLevel;
import com.JobPortal.JPP.entity.enums.JobStatus;
import com.JobPortal.JPP.entity.enums.JobType;
import com.JobPortal.JPP.entity.enums.WorkMode;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ==========================
       Basic Job Information
       ========================== */

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    /* ==========================
       Company Information
       ========================== */

    @Column(nullable = false)
    private String companyName;

    private String companyLogo;

    /* ==========================
       Recruiter
       ========================== */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User recruiter;

    /* ==========================
       Job Details
       ========================== */

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;// On-site, Remote, Hybrid

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperienceLevel experienceLevel;

    private Integer minExperience;

    private Integer maxExperience;

    /* ==========================
       Salary
       ========================== */

    private Double minSalary;

    private Double maxSalary;

    /* ==========================
       Skills
       ========================== */


    @OneToMany(
            mappedBy = "job",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<JobSkill> jobSkills = new ArrayList<>();

    /* ==========================
       Other Information
       ========================== */

    private Integer vacancies;

    private LocalDate applicationDeadline;

    /* ==========================
       Timestamps
       ========================== */

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /* ==========================
       Lifecycle Hooks
       ========================== */

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.ACTIVE;
}