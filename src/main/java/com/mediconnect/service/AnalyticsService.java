package com.mediconnect.service;

import com.mediconnect.dto.response.AnalyticsResponse;

public interface AnalyticsService {
    AnalyticsResponse getPlatformAnalytics();
    AnalyticsResponse getDoctorAnalytics(Long doctorId);
}