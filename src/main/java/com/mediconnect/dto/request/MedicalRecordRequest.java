package com.mediconnect.dto.request;

import com.mediconnect.entity.RecordCategory;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MedicalRecordRequest {

    private RecordCategory category;

    private String doctorName;

    private String labName;

    private LocalDate recordDate;

    private String notes;
}