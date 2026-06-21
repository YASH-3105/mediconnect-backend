package com.mediconnect.controller;

import com.mediconnect.dto.request.UpdateUserRequest;
import com.mediconnect.dto.response.AdminDashboardResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.UserResponse;
import com.mediconnect.entity.Role;
import com.mediconnect.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/users")
    public ResponseEntity<PagedResponse<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getAllUsers(page, size));
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<PagedResponse<UserResponse>> getUsersByRole(
            @PathVariable Role role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getUsersByRole(role, page, size));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(adminService.updateUser(userId, request));
    }

    @PatchMapping("/users/{userId}/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.deactivateUser(userId));
    }

    @PatchMapping("/users/{userId}/activate")
    public ResponseEntity<UserResponse> activateUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.activateUser(userId));
    }

    @PatchMapping("/users/{userId}/unlock")
    public ResponseEntity<UserResponse> unlockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.unlockUser(userId));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}