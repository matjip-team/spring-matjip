package com.restaurant.matjip.data.controller;

import com.restaurant.matjip.data.dto.RestaurantCreateRequest;
import com.restaurant.matjip.data.dto.RestaurantListDTO;
import com.restaurant.matjip.data.dto.RestaurantMapDTO;
import com.restaurant.matjip.data.dto.RestaurantMyRequestDTO;
import com.restaurant.matjip.data.dto.RestaurantSearchRequest;
import com.restaurant.matjip.data.service.RestaurantService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

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
    }
}
