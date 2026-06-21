package com.mediconnect.service;

import com.mediconnect.dto.response.NotificationResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.UnreadCountResponse;
import com.mediconnect.entity.NotificationType;
import com.mediconnect.entity.User;

public interface NotificationService {
    void sendNotification(User user, String title, String message,
                          NotificationType type, String deepLink);
    PagedResponse<NotificationResponse> getUserNotifications(Long userId, int page, int size);
    PagedResponse<NotificationResponse> getUnreadNotifications(Long userId, int page, int size);
    UnreadCountResponse getUnreadCount(Long userId);
    NotificationResponse markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
}