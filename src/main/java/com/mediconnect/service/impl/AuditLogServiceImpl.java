package com.mediconnect.service.impl;

import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.AuditLog;
import com.mediconnect.repository.AuditLogRepository;
import com.mediconnect.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(Long userId, String userEmail, String userRole,
                    String action, String entityType, Long entityId,
                    String description) {
        AuditLog auditLog = AuditLog.builder()
                .userId(userId)
                .userEmail(userEmail)
                .userRole(userRole)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .description(description)
                .build();

        auditLogRepository.save(auditLog);
        log.debug("Audit log saved: {} - {} - {}", userEmail, action, entityType);
    }

    @Override
    public PagedResponse<AuditLog> getLogs(Long userId, String action,
                                            String entityType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> result = auditLogRepository.searchLogs(
                userId, action, entityType, pageable);
        return buildPagedResponse(result);
    }

    @Override
    public PagedResponse<AuditLog> getAllLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        Page<AuditLog> result = auditLogRepository.findAll(pageable);
        return buildPagedResponse(result);
    }

    private PagedResponse<AuditLog> buildPagedResponse(Page<AuditLog> page) {
        List<AuditLog> content = page.getContent();
        return PagedResponse.<AuditLog>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}