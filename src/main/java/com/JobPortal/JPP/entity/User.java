package com.JobPortal.JPP.entity;

import com.JobPortal.JPP.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private  String password;
    @Enumerated(EnumType.STRING)
    private Role role;

}
