package com.restaurant.matjip.data.dto;

import com.restaurant.matjip.data.domain.RestaurantApprovalStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RestaurantApprovalUpdateRequest {

    @NotNull(message = "Approval status is required")
    private RestaurantApprovalStatus status;
}
