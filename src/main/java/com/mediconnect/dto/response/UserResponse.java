package com.mediconnect.dto.response;

import com.mediconnect.entity.Role;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private boolean enabled;
    private boolean accountLocked;
    private int failedLoginAttempts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}