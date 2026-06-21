package com.mediconnect.service.impl;

import com.mediconnect.dto.request.LoginRequest;
import com.mediconnect.dto.request.RegisterRequest;
import com.mediconnect.dto.response.ApiResponse;
import com.mediconnect.dto.response.AuthResponse;
import com.mediconnect.entity.User;
import com.mediconnect.exception.BadRequestException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.UserRepository;
import com.mediconnect.service.AuthService;
import com.mediconnect.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public ApiResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered. Please use a different email.");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole())
                .enabled(true)
                .accountLocked(false)
                .emailVerificationToken(UUID.randomUUID().toString())
                .build();

        userRepository.save(user);
        log.info("User registered successfully: {}", request.getEmail());

        return ApiResponse.builder()
                .success(true)
                .message("Registration successful! Please verify your email.")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with this email."));

        if (user.isAccountLocked()) {
            throw new BadRequestException("Account is locked. Please contact support.");
        }

        if (!user.isEnabled()) {
            throw new BadRequestException("Account is not verified. Please verify your email.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);
            if (attempts >= 5) {
                user.setAccountLocked(true);
                log.warn("Account locked due to too many failed attempts: {}", request.getEmail());
            }
            userRepository.save(user);
            throw new BadRequestException("Invalid password. " + (5 - attempts) + " attempts remaining.");
        }

        // Reset failed attempts on success
        user.setFailedLoginAttempts(0);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        log.info("Login successful for: {}", request.getEmail());

        return AuthResponse.builder() 
        		.id(user.getId())
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .message("Login successful!")
                .build();
    }

    @Override
    public ApiResponse verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired verification token."));

        user.setEnabled(true);
        user.setEmailVerificationToken(null);
        userRepository.save(user);

        return ApiResponse.builder()
                .success(true)
                .message("Email verified successfully! You can now log in.")
                .build();
    }
}