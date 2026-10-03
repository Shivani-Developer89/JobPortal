package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.NotificationRequestDTO;
import com.JobPortal.JPP.dto.response.NotificationResponseDTO;

import java.util.List;

public interface NotificationService {

    NotificationResponseDTO createNotification(
            NotificationRequestDTO request
    );

    List<NotificationResponseDTO> getMyNotifications();

    long getUnreadCount();

    void markAsRead(Long notificationId);

    void markAllAsRead();
}