package com.JobPortal.JPP.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "experience")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;

    private String jobTitle;

    private String employmentType;

    private String location;

    private String yearsOfExperience;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(columnDefinition = "TEXT")
    private String achievements;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private CandidateProfile candidateProfile;
}
