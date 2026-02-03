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

    private Long getUserId(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }

    /* 리뷰 작성 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> create(
            @PathVariable Long restaurantId,
            @RequestBody ReviewCreateRequest request,
            Authentication authentication
    ) {
        reviewService.create(getUserId(authentication), restaurantId, request);
        return ApiResponse.success(null);
    }

    /* 리뷰 목록 */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ReviewResponse>> list(
            @PathVariable Long restaurantId
    ) {
        return ApiResponse.success(reviewService.getReviews(restaurantId));
    }

    /* 평균 평점 */
    @GetMapping("/rating")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Double> rating(
            @PathVariable Long restaurantId
    ) {
        return ApiResponse.success(reviewService.getAverageRating(restaurantId));
    }
}
