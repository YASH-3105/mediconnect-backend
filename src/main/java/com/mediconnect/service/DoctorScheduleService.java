package com.mediconnect.service;

import com.mediconnect.dto.request.DoctorScheduleRequest;
import com.mediconnect.dto.request.LeaveBlockRequest;
import com.mediconnect.dto.response.DoctorScheduleResponse;
import com.mediconnect.dto.response.LeaveBlockResponse;
import java.time.LocalDate;
import java.util.List;

public interface DoctorScheduleService {
    DoctorScheduleResponse createOrUpdateSchedule(Long doctorId, DoctorScheduleRequest request);
    List<DoctorScheduleResponse> getScheduleByDoctorId(Long doctorId);
    DoctorScheduleResponse deactivateSchedule(Long scheduleId);
    LeaveBlockResponse addLeaveBlock(Long doctorId, LeaveBlockRequest request);
    List<LeaveBlockResponse> getLeaveBlocks(Long doctorId);
    void removeLeaveBlock(Long leaveBlockId);
    List<String> getAvailableSlots(Long doctorId, LocalDate date);
}