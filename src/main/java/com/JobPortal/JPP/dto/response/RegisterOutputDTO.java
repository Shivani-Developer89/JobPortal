package com.JobPortal.JPP.dto.response;

import com.JobPortal.JPP.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterOutputDTO {
    private Long id;
    private String name;

    private String email;
    private  String password;

    private Role role;
}
