package com.mediconnect.service.impl;

import com.mediconnect.dto.response.NotificationResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.UnreadCountResponse;
import com.mediconnect.entity.*;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.NotificationRepository;
import com.mediconnect.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void sendNotification(User user, String title, String message,
                                  NotificationType type, String deepLink) {
        log.info("Sending notification to userId: {} - {}", user.getId(), title);

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .deepLink(deepLink)
                .read(false)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public PagedResponse<NotificationResponse> getUserNotifications(
            Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> result =
                notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return buildPagedResponse(result);
    }

    @Override
    public PagedResponse<NotificationResponse> getUnreadNotifications(
            Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> result =
                notificationRepository
                        .findByUserIdAndReadFalseOrderByCreatedAtDesc(userId, pageable);
        return buildPagedResponse(result);
    }

    @Override
    public UnreadCountResponse getUnreadCount(Long userId) {
        long count = notificationRepository.countByUserIdAndReadFalse(userId);
        return UnreadCountResponse.builder()
                .userId(userId)
                .unreadCount(count)
                .build();
    }

    @Override
    public NotificationResponse markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with id: " + notificationId));
        notification.setRead(true);
        return mapToResponse(notificationRepository.save(notification));
    }

    @Override
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsReadByUserId(userId);
        log.info("All notifications marked as read for userId: {}", userId);
    }

    private PagedResponse<NotificationResponse> buildPagedResponse(Page<Notification> page) {
        List<NotificationResponse> content = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<NotificationResponse>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private NotificationResponse mapToResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .userId(n.getUser().getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .type(n.getType())
                .deepLink(n.getDeepLink())
                .read(n.isRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}