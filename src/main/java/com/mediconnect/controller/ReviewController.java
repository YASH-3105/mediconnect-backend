package com.mediconnect.controller;

import com.mediconnect.dto.request.ReviewRequest;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.ReviewResponse;
import com.mediconnect.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/patient/{patientId}")
    public ResponseEntity<ReviewResponse> submitReview(
            @PathVariable Long patientId,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.submitReview(patientId, request));
    }

    @PatchMapping("/{reviewId}/doctor-response")
    public ResponseEntity<ReviewResponse> addDoctorResponse(
            @PathVariable Long reviewId,
            @RequestParam String response) {
        return ResponseEntity.ok(reviewService.getDoctorResponse(reviewId, response));
    }

    @PatchMapping("/{reviewId}/flag")
    public ResponseEntity<ReviewResponse> flagReview(
            @PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.flagReview(reviewId));
    }

    @PatchMapping("/{reviewId}/hide")
    public ResponseEntity<ReviewResponse> hideReview(
            @PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.hideReview(reviewId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<PagedResponse<ReviewResponse>> getDoctorReviews(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                reviewService.getDoctorReviews(doctorId, page, size));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getById(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getById(reviewId));
    }
}