package com.restaurant.matjip.data.controller;

import com.restaurant.matjip.data.domain.RestaurantApprovalStatus;
import com.restaurant.matjip.data.dto.RestaurantAdminDetailDTO;
import com.restaurant.matjip.data.dto.RestaurantAdminListDTO;
import com.restaurant.matjip.data.dto.RestaurantApprovalUpdateRequest;
import com.restaurant.matjip.data.service.RestaurantService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/restaurants")
@RequiredArgsConstructor
public class AdminRestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public ApiResponse<List<RestaurantAdminListDTO>> getByApprovalStatus(
            @RequestParam(defaultValue = "PENDING") RestaurantApprovalStatus status,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        return ApiResponse.success(
                restaurantService.getByApprovalStatusForAdmin(status, userDetails.getId())
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<RestaurantAdminDetailDTO> getRequestDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        return ApiResponse.success(restaurantService.getRequestDetailForAdmin(id, userDetails.getId()));
    }

    @GetMapping("/{id}/license-view-url")
    public ApiResponse<String> getLicenseViewUrl(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        return ApiResponse.success(restaurantService.getLicenseViewUrlForAdmin(id, userDetails.getId()));
    }

    @PatchMapping("/{id}/approval")
    public ApiResponse<Void> updateApproval(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantApprovalUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        restaurantService.updateApprovalStatus(
                id,
                request.getStatus(),
                request.getRejectedReason(),
                userDetails.getId()
        );
        return ApiResponse.success(null);
    }
}
