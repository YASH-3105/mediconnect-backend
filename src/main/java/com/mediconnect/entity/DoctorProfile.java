package com.mediconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialization specialization;

    @Column(nullable = false)
    private String qualification;

    private String clinicName;

    private String clinicAddress;

    private String city;

    private String languagesSpoken;

    private Integer experienceYears;

    private Double consultationFee;

    private Double averageRating;

    private Integer totalRatings;

    @Column(nullable = false)
    private boolean acceptingNewPatients = true;

    @Column(nullable = false)
    private boolean approved = false;

    private String profilePhotoUrl;

    @Column(length = 1000)
    private String bio;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (averageRating == null) averageRating = 0.0;
        if (totalRatings == null) totalRatings = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}