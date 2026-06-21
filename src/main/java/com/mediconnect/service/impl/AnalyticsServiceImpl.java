package com.mediconnect.service.impl;

import com.mediconnect.dto.response.AnalyticsResponse;
import com.mediconnect.entity.*;
import com.mediconnect.repository.*;
import com.mediconnect.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Override
    public AnalyticsResponse getPlatformAnalytics() {
        log.info("Generating platform analytics");

        List<Appointment> allAppointments = appointmentRepository.findAll();
        List<Payment> allPayments = paymentRepository.findAll();

        long total = allAppointments.size();
        long completed = countByStatus(allAppointments, AppointmentStatus.COMPLETED);
        long cancelled = countByStatus(allAppointments, AppointmentStatus.CANCELLED);
        long pending = countByStatus(allAppointments, AppointmentStatus.PENDING);

        double totalRevenue = allPayments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        double avgFee = allPayments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .average()
                .orElse(0.0);

        Map<String, Long> byStatus = allAppointments.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getStatus().name(), Collectors.counting()));

        Map<String, Long> byType = allAppointments.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getConsultationType().name(), Collectors.counting()));

        Map<String, Double> revenueBySpec = allPayments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .collect(Collectors.groupingBy(
                        p -> p.getAppointment().getDoctor()
                                .getSpecialization().name(),
                        Collectors.summingDouble(
                                p -> p.getAmount() != null ? p.getAmount() : 0.0)));

        return AnalyticsResponse.builder()
                .totalAppointments(total)
                .completedAppointments(completed)
                .cancelledAppointments(cancelled)
                .pendingAppointments(pending)
                .totalRevenue(Math.round(totalRevenue * 100.0) / 100.0)
                .averageConsultationFee(Math.round(avgFee * 100.0) / 100.0)
                .totalPatients(userRepository.countByRole(Role.PATIENT))
                .totalDoctors(userRepository.countByRole(Role.DOCTOR))
                .totalPrescriptions(prescriptionRepository.count())
                .totalMedicalRecords(medicalRecordRepository.count())
                .appointmentsByStatus(byStatus)
                .appointmentsByConsultationType(byType)
                .revenueBySpecialization(revenueBySpec)
                .build();
    }

    @Override
    public AnalyticsResponse getDoctorAnalytics(Long doctorId) {
        log.info("Generating analytics for doctorId: {}", doctorId);

        List<Appointment> doctorAppointments = appointmentRepository
                .findAll()
                .stream()
                .filter(a -> a.getDoctor().getId().equals(doctorId))
                .collect(Collectors.toList());

        long total = doctorAppointments.size();
        long completed = countByStatus(doctorAppointments, AppointmentStatus.COMPLETED);
        long cancelled = countByStatus(doctorAppointments, AppointmentStatus.CANCELLED);
        long pending = countByStatus(doctorAppointments, AppointmentStatus.PENDING);

        List<Payment> doctorPayments = paymentRepository.findAll()
                .stream()
                .filter(p -> p.getAppointment().getDoctor().getId().equals(doctorId)
                        && p.getStatus() == PaymentStatus.SUCCESS)
                .collect(Collectors.toList());

        double totalRevenue = doctorPayments.stream()
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        Map<String, Long> byStatus = doctorAppointments.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getStatus().name(), Collectors.counting()));

        Map<String, Long> byType = doctorAppointments.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getConsultationType().name(), Collectors.counting()));

        return AnalyticsResponse.builder()
                .totalAppointments(total)
                .completedAppointments(completed)
                .cancelledAppointments(cancelled)
                .pendingAppointments(pending)
                .totalRevenue(Math.round(totalRevenue * 100.0) / 100.0)
                .appointmentsByStatus(byStatus)
                .appointmentsByConsultationType(byType)
                .build();
    }

    private long countByStatus(List<Appointment> appointments,
                                AppointmentStatus status) {
        return appointments.stream()
                .filter(a -> a.getStatus() == status)
                .count();
    }
}