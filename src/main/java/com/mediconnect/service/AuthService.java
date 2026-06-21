package com.mediconnect.service;

import com.mediconnect.dto.request.LoginRequest;
import com.mediconnect.dto.request.RegisterRequest;
import com.mediconnect.dto.response.ApiResponse;
import com.mediconnect.dto.response.AuthResponse;

public interface AuthService {
    ApiResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    ApiResponse verifyEmail(String token);
}