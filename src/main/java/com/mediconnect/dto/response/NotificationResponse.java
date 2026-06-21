package com.mediconnect.dto.response;

import com.mediconnect.entity.NotificationType;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String title;
    private String message;
    private NotificationType type;
    private String deepLink;
    private boolean read;
    private LocalDateTime createdAt;
}