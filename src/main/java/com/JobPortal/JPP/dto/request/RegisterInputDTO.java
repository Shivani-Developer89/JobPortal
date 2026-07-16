package com.JobPortal.JPP.dto.request;

import com.JobPortal.JPP.entity.enums.Role;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterInputDTO {

    private String name;

    private String email;
    private  String password;


    private Role role;
    private String resumePath;


}
