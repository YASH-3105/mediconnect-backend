package com.mediconnect.controller;

import com.mediconnect.service.CsvService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/export")
@RequiredArgsConstructor
public class CsvController {

    private final CsvService csvService;

    @GetMapping("/patients")
    public void exportPatients(HttpServletResponse response) throws IOException {
        csvService.exportPatients(response);
    }

    @GetMapping("/appointments")
    public void exportAppointments(HttpServletResponse response) throws IOException {
        csvService.exportAppointments(response);
    }

    @GetMapping("/payments")
    public void exportPayments(HttpServletResponse response) throws IOException {
        csvService.exportPayments(response);
    }

    @GetMapping("/doctors")
    public void exportDoctors(HttpServletResponse response) throws IOException {
        csvService.exportDoctors(response);
    }
}