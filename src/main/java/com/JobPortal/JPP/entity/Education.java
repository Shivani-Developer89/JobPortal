package com.JobPortal.JPP.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "education")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private CandidateProfile candidateProfile;

    private String level;

    private String school;

    private String board;

    private String stream;

    private String degree;

    private String branch;

    private String college;

    private String university;

    private Integer passingYear;

    private String gradingType;

    private String score;
}
