package com.mediconnect.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyInventoryResponse {
    private Long id;
    private Long pharmacyUserId;
    private String pharmacyName;
    private String medicineName;
    private String genericName;
    private String manufacturer;
    private Integer stockQuantity;
    private Integer reorderThreshold;
    private Double unitPrice;
    private boolean active;
    private boolean lowStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}