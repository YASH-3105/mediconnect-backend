package com.mediconnect.service;

import com.mediconnect.dto.request.ReviewRequest;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.ReviewResponse;

public interface ReviewService {
    ReviewResponse submitReview(Long patientId, ReviewRequest request);
    ReviewResponse getDoctorResponse(Long reviewId, String response);
    ReviewResponse flagReview(Long reviewId);
    ReviewResponse hideReview(Long reviewId);
    PagedResponse<ReviewResponse> getDoctorReviews(Long doctorId, int page, int size);
    ReviewResponse getById(Long reviewId);
}