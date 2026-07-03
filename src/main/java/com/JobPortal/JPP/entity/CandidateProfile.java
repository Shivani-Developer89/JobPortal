package com.JobPortal.JPP.entity;
import jakarta.persistence.*;
import lombok.Data;

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

    private String education;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(columnDefinition = "TEXT")
    private String experience;

    private String github;

    private String linkedin;
}