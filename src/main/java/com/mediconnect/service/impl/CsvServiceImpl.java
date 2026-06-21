package com.mediconnect.service.impl;

import com.mediconnect.entity.*;
import com.mediconnect.repository.*;
import com.mediconnect.service.CsvService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvServiceImpl implements CsvService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void exportPatients(HttpServletResponse response) throws IOException {
        setHeaders(response, "patients_export");
        PrintWriter writer = response.getWriter();

        writer.println("ID,Full Name,Email,Phone,Enabled,Created At");

        List<User> patients = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == Role.PATIENT)
                .toList();

        for (User u : patients) {
            writer.println(String.join(",",
                    String.valueOf(u.getId()),
                    escape(u.getFullName()),
                    escape(u.getEmail()),
                    escape(u.getPhone()),
                    String.valueOf(u.isEnabled()),
                    u.getCreatedAt() != null ?
                            u.getCreatedAt().format(FORMATTER) : ""
            ));
        }
        writer.flush();
        log.info("Exported {} patients", patients.size());
    }

    @Override
    public void exportAppointments(HttpServletResponse response) throws IOException {
        setHeaders(response, "appointments_export");
        PrintWriter writer = response.getWriter();

        writer.println("ID,Reference,Patient,Doctor,Date,Time,Type,Status,Fee");

        List<Appointment> appointments = appointmentRepository.findAll();

        for (Appointment a : appointments) {
            writer.println(String.join(",",
                    String.valueOf(a.getId()),
                    escape(a.getAppointmentReference()),
                    escape(a.getPatient().getFullName()),
                    escape(a.getDoctor().getUser().getFullName()),
                    a.getAppointmentDate().toString(),
                    a.getAppointmentTime().toString(),
                    a.getConsultationType().name(),
                    a.getStatus().name(),
                    a.getFee() != null ? String.valueOf(a.getFee()) : "0"
            ));
        }
        writer.flush();
        log.info("Exported {} appointments", appointments.size());
    }

    @Override
    public void exportPayments(HttpServletResponse response) throws IOException {
        setHeaders(response, "payments_export");
        PrintWriter writer = response.getWriter();

        writer.println("ID,Transaction ID,Patient,Doctor,Amount,Currency," +
                "Method,Status,Paid At");

        List<Payment> payments = paymentRepository.findAll();

        for (Payment p : payments) {
            writer.println(String.join(",",
                    String.valueOf(p.getId()),
                    escape(p.getTransactionId()),
                    escape(p.getPatient().getFullName()),
                    escape(p.getAppointment().getDoctor().getUser().getFullName()),
                    String.valueOf(p.getAmount()),
                    escape(p.getCurrency()),
                    p.getPaymentMethod().name(),
                    p.getStatus().name(),
                    p.getPaidAt() != null ? p.getPaidAt().format(FORMATTER) : ""
            ));
        }
        writer.flush();
        log.info("Exported {} payments", payments.size());
    }

    @Override
    public void exportDoctors(HttpServletResponse response) throws IOException {
        setHeaders(response, "doctors_export");
        PrintWriter writer = response.getWriter();

        writer.println("ID,Full Name,Email,Specialization,City," +
                "Experience,Fee,Rating,Approved");

        List<DoctorProfile> doctors = doctorProfileRepository.findAll();

        for (DoctorProfile d : doctors) {
            writer.println(String.join(",",
                    String.valueOf(d.getId()),
                    escape(d.getUser().getFullName()),
                    escape(d.getUser().getEmail()),
                    d.getSpecialization().name(),
                    escape(d.getCity()),
                    String.valueOf(d.getExperienceYears()),
                    String.valueOf(d.getConsultationFee()),
                    String.valueOf(d.getAverageRating()),
                    String.valueOf(d.isApproved())
            ));
        }
        writer.flush();
        log.info("Exported {} doctors", doctors.size());
    }

    private void setHeaders(HttpServletResponse response, String filename) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition",
                "attachment; filename=" + filename + "_" + timestamp + ".csv");
    }

    private String escape(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}