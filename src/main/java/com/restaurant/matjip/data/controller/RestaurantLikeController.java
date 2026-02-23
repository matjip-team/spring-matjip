package com.restaurant.matjip.data.controller;

import com.restaurant.matjip.data.service.RestaurantLikeService;
import com.restaurant.matjip.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/likes")
@RequiredArgsConstructor
public class RestaurantLikeController {

    private final RestaurantLikeService restaurantLikeService;

    /* 🔥 JWT에서 email 추출 */
    private String getUserEmail(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        // anonymousUser 방지
        if ("anonymousUser".equals(authentication.getName())) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return authentication.getName();  // email 반환
    }

    /* ================= 좋아요 ================= */

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> like(
            @PathVariable Long restaurantId,
            Authentication authentication
    ) {
        String email = getUserEmail(authentication);
        restaurantLikeService.likeByEmail(email, restaurantId);
        return ApiResponse.success(null);
    }

    /* ================= 좋아요 취소 ================= */

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> unlike(
            @PathVariable Long restaurantId,
            Authentication authentication
    ) {
        String email = getUserEmail(authentication);
        restaurantLikeService.unlikeByEmail(email, restaurantId);
        return ApiResponse.success(null);
    }

    /* ================= 좋아요 개수 ================= */

    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Long> count(@PathVariable Long restaurantId) {
        return ApiResponse.success(
                restaurantLikeService.count(restaurantId)
        );
    }
}
