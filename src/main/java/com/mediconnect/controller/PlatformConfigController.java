package com.mediconnect.controller;

import com.mediconnect.dto.request.PlatformConfigRequest;
import com.mediconnect.dto.response.PlatformConfigResponse;
import com.mediconnect.service.PlatformConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/config")
@RequiredArgsConstructor
public class PlatformConfigController {

    private final PlatformConfigService configService;

    @PostMapping
    public ResponseEntity<PlatformConfigResponse> createOrUpdate(
            @Valid @RequestBody PlatformConfigRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(configService.createOrUpdate(request));
    }

    @GetMapping
    public ResponseEntity<List<PlatformConfigResponse>> getAll() {
        return ResponseEntity.ok(configService.getAll());
    }

    @GetMapping("/key/{key}")
    public ResponseEntity<PlatformConfigResponse> getByKey(
            @PathVariable String key) {
        return ResponseEntity.ok(configService.getByKey(key));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<PlatformConfigResponse>> getByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(configService.getByCategory(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        configService.delete(id);
        return ResponseEntity.noContent().build();
    }
}