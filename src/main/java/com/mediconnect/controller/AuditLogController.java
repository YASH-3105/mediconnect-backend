package com.mediconnect.controller;

import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.AuditLog;
import com.mediconnect.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<PagedResponse<AuditLog>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(auditLogService.getAllLogs(page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponse<AuditLog>> searchLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(
                auditLogService.getLogs(userId, action, entityType, page, size));
    }
}