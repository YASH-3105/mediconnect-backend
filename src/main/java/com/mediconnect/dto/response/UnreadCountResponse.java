package com.mediconnect.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnreadCountResponse {
    private Long userId;
    private long unreadCount;
}