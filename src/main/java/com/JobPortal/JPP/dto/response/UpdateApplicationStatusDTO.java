package com.JobPortal.JPP.dto.response;

import com.JobPortal.JPP.entity.enums.ApplicationStatus;
import lombok.Data;

@Data
public class UpdateApplicationStatusDTO {
    private ApplicationStatus status;
}
