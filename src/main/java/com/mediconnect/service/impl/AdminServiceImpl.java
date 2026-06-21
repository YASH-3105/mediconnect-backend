package com.mediconnect.service.impl;

import com.mediconnect.dto.request.UpdateUserRequest;
import com.mediconnect.dto.response.AdminDashboardResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.UserResponse;
import com.mediconnect.entity.*;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.*;
import com.mediconnect.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public PagedResponse<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        Page<User> result = userRepository.findAll(pageable);
        return buildPagedResponse(result);
    }

    @Override
    public PagedResponse<UserResponse> getUsersByRole(Role role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        Page<User> result = userRepository.findByRole(role, pageable);
        return buildPagedResponse(result);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return mapToResponse(user);
    }

    @Override
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        log.info("Admin updating userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setEnabled(request.isEnabled());
        user.setAccountLocked(request.isAccountLocked());

        return mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse deactivateUser(Long userId) {
        log.info("Admin deactivating userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        user.setEnabled(false);
        return mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse activateUser(Long userId) {
        log.info("Admin activating userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        user.setEnabled(true);
        return mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse unlockUser(Long userId) {
        log.info("Admin unlocking userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        return mapToResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Admin deleting userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        user.setEnabled(false);
        user.setAccountLocked(true);
        userRepository.save(user);
    }

    @Override
    public AdminDashboardResponse getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalPatients = userRepository.countByRole(Role.PATIENT);
        long totalDoctors = userRepository.countByRole(Role.DOCTOR);
        long totalPharmacies = userRepository.countByRole(Role.PHARMACY);
        long totalAppointments = appointmentRepository.count();
        long pendingApprovals = doctorProfileRepository.countByApprovedFalse();
        long totalPayments = paymentRepository.count();

        long totalAppointmentsToday = appointmentRepository.findAll()
                .stream()
                .filter(a -> a.getAppointmentDate().equals(LocalDate.now()))
                .count();
        
        double totalRevenue = paymentRepository.findAll().stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        return AdminDashboardResponse.builder()
                .totalUsers(totalUsers)
                .totalPatients(totalPatients)
                .totalDoctors(totalDoctors)
                .totalPharmacies(totalPharmacies)
                .totalAppointments(totalAppointments)
                .totalAppointmentsToday(totalAppointmentsToday)
                .pendingDoctorApprovals(pendingApprovals)
                .totalPayments(totalPayments)
                .totalRevenue(totalRevenue)
                .build();
    }

    private PagedResponse<UserResponse> buildPagedResponse(Page<User> page) {
        List<UserResponse> content = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<UserResponse>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private UserResponse mapToResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .fullName(u.getFullName())
                .email(u.getEmail())
                .phone(u.getPhone())
                .role(u.getRole())
                .enabled(u.isEnabled())
                .accountLocked(u.isAccountLocked())
                .failedLoginAttempts(u.getFailedLoginAttempts())
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .build();
    }
}