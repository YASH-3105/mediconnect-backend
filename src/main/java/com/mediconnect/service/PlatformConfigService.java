package com.mediconnect.service;

import com.mediconnect.dto.request.PlatformConfigRequest;
import com.mediconnect.dto.response.PlatformConfigResponse;
import java.util.List;

public interface PlatformConfigService {
    PlatformConfigResponse createOrUpdate(PlatformConfigRequest request);
    PlatformConfigResponse getByKey(String key);
    List<PlatformConfigResponse> getByCategory(String category);
    List<PlatformConfigResponse> getAll();
    void delete(Long id);
}