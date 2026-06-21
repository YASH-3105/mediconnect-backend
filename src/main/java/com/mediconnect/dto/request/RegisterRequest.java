package com.mediconnect.dto.request;

import com.mediconnect.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Please provide a valid fullName")
    private String fullName;

    @NotBlank(message = "Please provide a valid email")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Please provide a valid password")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Please provide a valid phone")
    @Pattern(regexp = "^[0-9]{10}$", message = "Please provide a valid phone")
    private String phone;

    @NotNull(message = "Please provide a valid role")
    private Role role;
}