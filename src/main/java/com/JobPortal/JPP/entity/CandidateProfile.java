package com.JobPortal.JPP.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "candidate_profiles")
public class CandidateProfile {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "candidate_id")
    private User candidate;

    private String phone;

    private String location;

    @OneToMany(
            mappedBy = "candidateProfile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Education> education = new ArrayList<>();

    @OneToMany(
            mappedBy = "candidateProfile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Experience> experience = new ArrayList<>();
    private String resumePath;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(columnDefinition = "TEXT")
    private String github;

    private String linkedin;

    private String leetcode;
}