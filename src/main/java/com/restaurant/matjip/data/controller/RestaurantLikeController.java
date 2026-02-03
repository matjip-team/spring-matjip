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

    /** JWT에서 userId 추출 */
    private Long getUserId(Authentication authentication) {
        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "JWT subject(name)가 userId가 아닙니다. Security 설정 확인 필요"
            );
        }
    }

    /* 좋아요 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> like(
            @PathVariable Long restaurantId,
            Authentication authentication
    ) {
        Long userId = getUserId(authentication);
        restaurantLikeService.like(userId, restaurantId);
        return ApiResponse.success(null);
    }

    /* 좋아요 취소 */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> unlike(
            @PathVariable Long restaurantId,
            Authentication authentication
    ) {
        Long userId = getUserId(authentication);
        restaurantLikeService.unlike(userId, restaurantId);
        return ApiResponse.success(null);
    }

    /* 좋아요 개수 */
    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Long> count(@PathVariable Long restaurantId) {
        return ApiResponse.success(
                restaurantLikeService.count(restaurantId)
        );
    }
}
