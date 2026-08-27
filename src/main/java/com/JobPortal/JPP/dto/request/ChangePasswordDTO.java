package com.JobPortal.JPP.dto.request;

import lombok.Data;

@Data
public class ChangePasswordDTO {

    private String currentPassword;

    private String newPassword;
}