package com.mediconnect.service;

import com.mediconnect.dto.request.UpdateUserRequest;
import com.mediconnect.dto.response.AdminDashboardResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.UserResponse;
import com.mediconnect.entity.Role;

public interface AdminService {
    PagedResponse<UserResponse> getAllUsers(int page, int size);
    PagedResponse<UserResponse> getUsersByRole(Role role, int page, int size);
    UserResponse getUserById(Long userId);
    UserResponse updateUser(Long userId, UpdateUserRequest request);
    UserResponse deactivateUser(Long userId);
    UserResponse activateUser(Long userId);
    UserResponse unlockUser(Long userId);
    void deleteUser(Long userId);
    AdminDashboardResponse getDashboardStats();
}