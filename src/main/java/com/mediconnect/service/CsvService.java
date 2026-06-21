package com.mediconnect.service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface CsvService {
    void exportPatients(HttpServletResponse response) throws IOException;
    void exportAppointments(HttpServletResponse response) throws IOException;
    void exportPayments(HttpServletResponse response) throws IOException;
    void exportDoctors(HttpServletResponse response) throws IOException;
}