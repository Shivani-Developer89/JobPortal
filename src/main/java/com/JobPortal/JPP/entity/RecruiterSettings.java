package com.JobPortal.JPP.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "recruiter_settings")
@Data
public class RecruiterSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false, unique = true)
    private User recruiter;

    // Notification preferences

    @Column(nullable = false)
    private boolean newApplicants = true;

    @Column(nullable = false)
    private boolean emailNotifications = true;

    @Column(nullable = false)
    private boolean jobExpiryReminders = true;

    // Job posting preferences

    private String defaultLocation;

    private String employmentType;

    private String salaryVisibility;
}