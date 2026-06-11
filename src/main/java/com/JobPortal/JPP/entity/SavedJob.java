package com.JobPortal.JPP.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User candidate;

    @ManyToOne
    private Job job;
}
