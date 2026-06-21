package com.mediconnect.service.impl;

import com.mediconnect.dto.request.PlatformConfigRequest;
import com.mediconnect.dto.response.PlatformConfigResponse;
import com.mediconnect.entity.PlatformConfig;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.PlatformConfigRepository;
import com.mediconnect.service.PlatformConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlatformConfigServiceImpl implements PlatformConfigService {

    private final PlatformConfigRepository configRepository;

    @Override
    public PlatformConfigResponse createOrUpdate(PlatformConfigRequest request) {
        log.info("Creating/updating config: {}", request.getConfigKey());

        PlatformConfig config = configRepository
                .findByConfigKey(request.getConfigKey())
                .orElse(PlatformConfig.builder().build());

        config.setConfigKey(request.getConfigKey());
        config.setConfigValue(request.getConfigValue());
        config.setDescription(request.getDescription());
        config.setCategory(request.getCategory());

        return mapToResponse(configRepository.save(config));
    }

    @Override
    public PlatformConfigResponse getByKey(String key) {
        PlatformConfig config = configRepository.findByConfigKey(key)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Config not found with key: " + key));
        return mapToResponse(config);
    }

    @Override
    public List<PlatformConfigResponse> getByCategory(String category) {
        return configRepository.findByCategory(category)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlatformConfigResponse> getAll() {
        return configRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        PlatformConfig config = configRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Config not found with id: " + id));
        configRepository.delete(config);
    }

    private PlatformConfigResponse mapToResponse(PlatformConfig c) {
        return PlatformConfigResponse.builder()
                .id(c.getId())
                .configKey(c.getConfigKey())
                .configValue(c.getConfigValue())
                .description(c.getDescription())
                .category(c.getCategory())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}