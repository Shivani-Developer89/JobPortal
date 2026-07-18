package com.JobPortal.JPP.entity;

import com.JobPortal.JPP.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @Column(unique = true)
    @Email
    private String email;
    @NotBlank
    private  String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String resumePath;
    @Column(name = "resume_uploaded_at")
    private LocalDateTime resumeUploadedAt;
    @Column(nullable = true)
    private String companyName;

}
