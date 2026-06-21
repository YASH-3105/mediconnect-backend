package com.mediconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_vitals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthVital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VitalType vitalType;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private String unit;

    private String notes;

    private boolean abnormal = false;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}