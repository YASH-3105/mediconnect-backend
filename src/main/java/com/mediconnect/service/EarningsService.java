package com.mediconnect.service;

import com.mediconnect.dto.response.DoctorEarningsResponse;

public interface EarningsService {
    DoctorEarningsResponse getDoctorEarnings(Long doctorId);
}