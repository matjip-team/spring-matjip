package com.restaurant.matjip.data.controller;

import com.restaurant.matjip.data.dto.ReviewCreateRequest;
import com.restaurant.matjip.data.dto.ReviewResponse;
import com.restaurant.matjip.data.service.ReviewService;
import com.restaurant.matjip.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    private String getUserEmail(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return authentication.getName();
    }

    /* ================= 리뷰 작성 ================= */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> create(
            @PathVariable Long restaurantId,
            @RequestBody ReviewCreateRequest request,
            Authentication authentication
    ) {
        String email = getUserEmail(authentication);
        reviewService.createByEmail(email, restaurantId, request);
        return ApiResponse.success(null);
    }

    /* ================= 리뷰 수정 ================= */
    @PutMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> update(
            @PathVariable Long restaurantId,
            @PathVariable Long reviewId,
            @RequestBody ReviewCreateRequest request,
            Authentication authentication
    ) {
        String email = getUserEmail(authentication);
        reviewService.updateByEmail(email, reviewId, request);
        return ApiResponse.success(null);
    }

    /* ================= 리뷰 삭제 ================= */
    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> delete(
            @PathVariable Long restaurantId,
            @PathVariable Long reviewId,
            Authentication authentication
    ) {
        String email = getUserEmail(authentication);
        reviewService.deleteByEmail(email, reviewId);
        return ApiResponse.success(null);
    }

    /* ================= 리뷰 목록 ================= */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ReviewResponse>> list(
            @PathVariable Long restaurantId,
            Authentication authentication
    ) {

        String email = null;

        if (authentication != null && authentication.isAuthenticated()) {
            email = authentication.getName();
        }

        return ApiResponse.success(
                reviewService.getReviews(restaurantId, email)
        );
    }

    /* ================= 평균 평점 ================= */
    @GetMapping("/rating")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Double> rating(
            @PathVariable Long restaurantId
    ) {
        return ApiResponse.success(
                reviewService.getAverageRating(restaurantId)
        );
    }
}
