package com.restaurant.matjip.data.controller;


import com.restaurant.matjip.data.dto.*;
import com.restaurant.matjip.data.service.RestaurantService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    /* ================= 목록 조회 ================= */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Page<RestaurantListDTO>> getRestaurants(
            RestaurantSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Authentication authentication
    ) {

        String email = extractEmail(authentication);

        return ApiResponse.success(
                restaurantService.search(request, page, size, email)
        );
    }

    /* ================= 지도 조회 ================= */
    @GetMapping("/map")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<RestaurantMapDTO>> getRestaurantsForMap(RestaurantSearchRequest request) {
        List<RestaurantMapDTO> response = restaurantService.searchForMap(request);
        return ApiResponse.success(response);

    }

    /* ================= 상세 조회 ================= */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<RestaurantDetailDTO> getRestaurantDetail(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String email = extractEmail(authentication);

        return ApiResponse.success(
                restaurantService.getDetail(id, email)
        );
    }

    /* ================= 로그인 이메일 추출 ================= */
    private String extractEmail(Authentication authentication) {

        if (authentication == null) {
            return null;
        }

        if (!authentication.isAuthenticated()) {
            return null;
        }

        if ("anonymousUser".equals(authentication.getName())) {
            return null;
        }

        return authentication.getName();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Long> createRestaurant(
            @Valid @RequestBody RestaurantCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        Long restaurantId = restaurantService.create(request, userDetails.getId());
        return ApiResponse.success(restaurantId);
    }

    @GetMapping("/requests/me")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<RestaurantMyRequestDTO>> getMyRequests(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        return ApiResponse.success(restaurantService.getMyRequests(userDetails.getId()));
    }

    @GetMapping("/requests/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<RestaurantMyRequestDetailDTO> getMyRequestDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        return ApiResponse.success(restaurantService.getMyRequestDetail(id, userDetails.getId()));
    }

    @GetMapping("/requests/{id}/license-view-url")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> getMyRequestLicenseViewUrl(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        return ApiResponse.success(restaurantService.getMyRequestLicenseViewUrl(id, userDetails.getId()));
    }

    @PatchMapping("/requests/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> cancelMyRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        restaurantService.cancelMyRequest(id, userDetails.getId());
        return ApiResponse.success(null);
    }

}
