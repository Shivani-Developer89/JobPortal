package com.JobPortal.JPP.dto.request;

import com.JobPortal.JPP.entity.enums.NotificationType;
import lombok.Data;

@Data
public class NotificationRequestDTO {

    private Long userId;
    private String title;
    private String message;
    private NotificationType type;
}