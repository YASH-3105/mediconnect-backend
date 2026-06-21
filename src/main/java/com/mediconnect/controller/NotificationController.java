package com.mediconnect.controller;

import com.mediconnect.dto.response.ApiResponse;
import com.mediconnect.dto.response.NotificationResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.UnreadCountResponse;
import com.mediconnect.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<PagedResponse<NotificationResponse>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(
                notificationService.getUserNotifications(userId, page, size));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<PagedResponse<NotificationResponse>> getUnreadNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(userId, page, size));
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<UnreadCountResponse> getUnreadCount(
            @PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.markAsRead(notificationId));
    }

    @PatchMapping("/user/{userId}/read-all")
    public ResponseEntity<ApiResponse> markAllAsRead(
            @PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(
                com.mediconnect.dto.response.ApiResponse.builder()
                        .success(true)
                        .message("All notifications marked as read.")
                        .build());
    }
}