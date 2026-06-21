package com.mediconnect.dto.request;

import com.mediconnect.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @NotBlank(message = "Please provide a valid fullName")
    private String fullName;

    @NotBlank(message = "Please provide a valid phone")
    @Pattern(regexp = "^[0-9]{10}$", message = "Please provide a valid phone")
    private String phone;

    @NotNull(message = "Please provide a valid role")
    private Role role;

    private boolean enabled;

    private boolean accountLocked;
}