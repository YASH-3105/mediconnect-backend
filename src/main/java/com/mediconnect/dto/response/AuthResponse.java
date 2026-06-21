package com.mediconnect.dto.response;

import com.mediconnect.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	private Long id;
    private String token;
    private String email;
    private String fullName;
    private Role role;
    private String message;
}