package com.JobPortal.JPP.dto.request;

import com.JobPortal.JPP.entity.enums.Role;
import lombok.Data;

@Data
public class RegisterInputDTO {

    private String name;

    private String email;
    private  String password;


    private Role role;
    private String resumePath;
}
