package com.mediconnect.service;

import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.AuditLog;

public interface AuditLogService {
    void log(Long userId, String userEmail, String userRole,
             String action, String entityType, Long entityId,
             String description);
    PagedResponse<AuditLog> getLogs(Long userId, String action,
                                     String entityType, int page, int size);
    PagedResponse<AuditLog> getAllLogs(int page, int size);
}