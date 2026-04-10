package com.agrovalue.backend.service;

import java.util.List;

import com.agrovalue.backend.dto.ReviewRequest;
import com.agrovalue.backend.dto.ReviewResponse;

public interface ReviewService {
    ReviewResponse addReview(ReviewRequest request);
    List<ReviewResponse> getReviewsByProduct(Long productId);
}
