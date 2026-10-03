package com.JobPortal.JPP.controller;

import com.JobPortal.JPP.dto.request.NotificationRequestDTO;
import com.JobPortal.JPP.dto.response.NotificationResponseDTO;
import com.JobPortal.JPP.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(
            @RequestBody NotificationRequestDTO request
    ) {
        return ResponseEntity.ok(
                notificationService.createNotification(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications() {

        return ResponseEntity.ok(
                notificationService.getMyNotifications()
        );
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {

        return ResponseEntity.ok(
                notificationService.getUnreadCount()
        );
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long notificationId
    ) {

        notificationService.markAsRead(notificationId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {

        notificationService.markAllAsRead();

        return ResponseEntity.ok().build();
    }
}