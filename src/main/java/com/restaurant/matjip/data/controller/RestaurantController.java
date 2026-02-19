package com.restaurant.matjip.data.controller;

<<<<<<< HEAD
import com.restaurant.matjip.data.dto.*;
=======
import com.restaurant.matjip.data.dto.RestaurantCreateRequest;
import com.restaurant.matjip.data.dto.RestaurantListDTO;
import com.restaurant.matjip.data.dto.RestaurantMapDTO;
import com.restaurant.matjip.data.dto.RestaurantMyRequestDTO;
import com.restaurant.matjip.data.dto.RestaurantSearchRequest;
>>>>>>> 2ef3ff8c5daeb273fac23afe690422d3601bf8ec
import com.restaurant.matjip.data.service.RestaurantService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
<<<<<<< HEAD
import org.springframework.security.core.Authentication;
=======
import org.springframework.security.core.annotation.AuthenticationPrincipal;
>>>>>>> 2ef3ff8c5daeb273fac23afe690422d3601bf8ec
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

<<<<<<< HEAD
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
    public ApiResponse<List<RestaurantMapDTO>> getRestaurantsForMap(
            RestaurantSearchRequest request
    ) {
        return ApiResponse.success(
                restaurantService.searchForMap(request)
        );
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
=======
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<RestaurantListDTO>> getRestaurants(RestaurantSearchRequest request) {
        List<RestaurantListDTO> response = restaurantService.search(request);
        return ApiResponse.success(response);
    }

    @GetMapping("/map")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<RestaurantMapDTO>> getRestaurantsForMap(RestaurantSearchRequest request) {
        List<RestaurantMapDTO> response = restaurantService.searchForMap(request);
        return ApiResponse.success(response);
>>>>>>> 2ef3ff8c5daeb273fac23afe690422d3601bf8ec
    }
}
