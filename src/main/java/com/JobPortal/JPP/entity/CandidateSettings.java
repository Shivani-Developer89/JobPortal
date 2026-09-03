package com.JobPortal.JPP.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "candidate_settings")
@Data
public class CandidateSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "candidate_id",
            nullable = false,
            unique = true
    )
    private User candidate;

    // =========================
    // Notification Preferences
    // =========================

    @Column(nullable = false)
    private boolean jobAlerts = true;

    @Column(nullable = false)
    private boolean emailNotifications = true;

    @Column(nullable = false)
    private boolean applicationUpdates = true;

    // =========================
    // Job Preferences
    // =========================

    private String preferredLocation;

    private String employmentType;

    private String workMode;
}