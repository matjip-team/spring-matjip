package com.restaurant.matjip.data.controller;

import com.restaurant.matjip.data.dto.RestaurantLicensePresignRequest;
import com.restaurant.matjip.data.dto.RestaurantLicensePresignResponse;
import com.restaurant.matjip.data.dto.RestaurantLicenseViewUrlRequest;
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

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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

    @PostMapping("/view-url")
    public ApiResponse<String> createViewUrl(
            @Valid @RequestBody RestaurantLicenseViewUrlRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        String fileKey = normalizeLicenseFileKey(request.getFileKey());
        if (!fileKey.startsWith("tmp/licenses/")) {
            throw new IllegalArgumentException("Invalid license file key");
        }

        return ApiResponse.success(restaurantLicenseFileService.createPresignedViewUrl(fileKey));
    }

    private String normalizeLicenseFileKey(String rawFileKey) {
        if (rawFileKey == null) {
            return "";
        }

        String value = rawFileKey.trim();
        if (value.isEmpty()) {
            return "";
        }

        if (value.startsWith("http://") || value.startsWith("https://")) {
            try {
                URI uri = URI.create(value);
                String path = uri.getPath();
                if (path != null && !path.isBlank()) {
                    value = path.startsWith("/") ? path.substring(1) : path;
                }
            } catch (Exception ignored) {
                // Keep original value and continue fallback parsing
            }
        }

        int tmpIdx = value.indexOf("tmp/licenses/");
        if (tmpIdx >= 0) {
            value = value.substring(tmpIdx);
        }

        return URLDecoder.decode(value, StandardCharsets.UTF_8).trim();
    }
}
