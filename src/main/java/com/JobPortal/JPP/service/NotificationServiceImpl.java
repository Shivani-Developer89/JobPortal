package com.JobPortal.JPP.service;

import com.JobPortal.JPP.dto.request.NotificationRequestDTO;
import com.JobPortal.JPP.dto.response.NotificationResponseDTO;
import com.JobPortal.JPP.entity.Notification;
import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.repository.NotificationRepository;
import com.JobPortal.JPP.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public NotificationResponseDTO createNotification(
            NotificationRequestDTO request
    ) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setRead(false);

        Notification saved =
                notificationRepository.save(notification);

        return convertToDTO(saved);
    }

    @Override
    public List<NotificationResponseDTO> getMyNotifications() {

        User currentUser = getCurrentUser();

        return notificationRepository
                .findByUserOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public long getUnreadCount() {

        User currentUser = getCurrentUser();

        return notificationRepository
                .countByUserAndReadFalse(currentUser);
    }

    @Override
    public void markAsRead(Long notificationId) {

        User currentUser = getCurrentUser();

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                )
                        );

        // Security check:
        // A user can only modify their own notification.
        if (!notification.getUser().getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "You are not allowed to modify this notification"
            );
        }

        notification.setRead(true);

        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead() {

        User currentUser = getCurrentUser();

        List<Notification> notifications =
                notificationRepository
                        .findByUserAndReadFalseOrderByCreatedAtDesc(
                                currentUser
                        );

        notifications.forEach(notification ->
                notification.setRead(true)
        );

        notificationRepository.saveAll(notifications);
    }

    private User getCurrentUser() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Current user not found"
                        )
                );
    }

    private NotificationResponseDTO convertToDTO(
            Notification notification
    ) {

        NotificationResponseDTO dto =
                new NotificationResponseDTO();

        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());

        return dto;
    }
}