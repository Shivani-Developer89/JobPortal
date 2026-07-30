package com.JobPortal.JPP.entity;

import com.JobPortal.JPP.entity.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class
Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @ManyToOne
    private User candidate;
    @ManyToOne
    private Job job;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private LocalDateTime appliedAt;


}
