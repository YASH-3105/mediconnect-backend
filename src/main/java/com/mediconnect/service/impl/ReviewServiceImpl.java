package com.mediconnect.service.impl;

import com.mediconnect.dto.request.ReviewRequest;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.ReviewResponse;
import com.mediconnect.entity.*;
import com.mediconnect.exception.BadRequestException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.*;
import com.mediconnect.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    @Override
    public ReviewResponse submitReview(Long patientId, ReviewRequest request) {
        log.info("Submitting review for appointmentId: {}", request.getAppointmentId());

        if (reviewRepository.existsByAppointmentId(request.getAppointmentId())) {
            throw new BadRequestException(
                    "You have already submitted a review for this appointment.");
        }

        Appointment appointment = appointmentRepository
                .findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + request.getAppointmentId()));

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new BadRequestException(
                    "You can only review completed appointments.");
        }

        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new BadRequestException(
                    "You are not authorized to review this appointment.");
        }

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + patientId));

        Review review = Review.builder()
                .patient(patient)
                .doctor(appointment.getDoctor())
                .appointment(appointment)
                .rating(request.getRating())
                .reviewText(request.getReviewText())
                .build();

        Review saved = reviewRepository.save(review);

        // Update doctor average rating
        updateDoctorRating(appointment.getDoctor().getId());

        return mapToResponse(saved);
    }

    @Override
    public ReviewResponse getDoctorResponse(Long reviewId, String response) {
        Review review = getReviewEntity(reviewId);
        review.setDoctorResponse(response);
        return mapToResponse(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse flagReview(Long reviewId) {
        Review review = getReviewEntity(reviewId);
        review.setFlagged(true);
        return mapToResponse(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse hideReview(Long reviewId) {
        Review review = getReviewEntity(reviewId);
        review.setHidden(true);
        reviewRepository.save(review);
        updateDoctorRating(review.getDoctor().getId());
        return mapToResponse(review);
    }

    @Override
    public PagedResponse<ReviewResponse> getDoctorReviews(
            Long doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        Page<Review> result =
                reviewRepository.findByDoctorIdAndHiddenFalse(doctorId, pageable);

        List<ReviewResponse> content = result.getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<ReviewResponse>builder()
                .content(content)
                .pageNumber(result.getNumber())
                .pageSize(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .last(result.isLast())
                .build();
    }

    @Override
    public ReviewResponse getById(Long reviewId) {
        return mapToResponse(getReviewEntity(reviewId));
    }

    private void updateDoctorRating(Long doctorId) {
        Double avgRating = reviewRepository.getAverageRatingByDoctorId(doctorId);
        long totalRatings = reviewRepository.countByDoctorId(doctorId);

        doctorProfileRepository.findById(doctorId).ifPresent(doctor -> {
            doctor.setAverageRating(avgRating != null ?
                    Math.round(avgRating * 10.0) / 10.0 : 0.0);
            doctor.setTotalRatings((int) totalRatings);
            doctorProfileRepository.save(doctor);
        });
    }

    private Review getReviewEntity(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Review not found with id: " + reviewId));
    }

    private ReviewResponse mapToResponse(Review r) {
        return ReviewResponse.builder()
                .id(r.getId())
                .patientId(r.getPatient().getId())
                .patientName(r.getPatient().getFullName())
                .doctorId(r.getDoctor().getId())
                .doctorName(r.getDoctor().getUser().getFullName())
                .appointmentId(r.getAppointment().getId())
                .rating(r.getRating())
                .reviewText(r.getReviewText())
                .doctorResponse(r.getDoctorResponse())
                .flagged(r.isFlagged())
                .hidden(r.isHidden())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}