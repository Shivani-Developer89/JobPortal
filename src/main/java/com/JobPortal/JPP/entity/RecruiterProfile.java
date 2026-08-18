package com.JobPortal.JPP.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "recruiter_profiles")
public class RecruiterProfile {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    private String phone;

    private String designation;

    private String companyName;

    private String companyWebsite;

    private String companyLocation;

    @Column(columnDefinition = "TEXT")
    private String companyDescription;

    private String profileImagePath;
}