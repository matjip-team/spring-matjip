package com.restaurant.matjip.data.controller;

import com.restaurant.matjip.data.dto.RestaurantLicensePresignRequest;
import com.restaurant.matjip.data.dto.RestaurantLicensePresignResponse;
import com.restaurant.matjip.data.service.RestaurantLicenseFileService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants/licenses")
@RequiredArgsConstructor
public class RestaurantLicenseController {

    private final RestaurantLicenseFileService restaurantLicenseFileService;

    @PostMapping("/presigned-url")
    public ApiResponse<RestaurantLicensePresignResponse> createPresignedUrl(
            @Valid @RequestBody RestaurantLicensePresignRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        return ApiResponse.success(restaurantLicenseFileService.createPresignedUpload(request));
    }
}
