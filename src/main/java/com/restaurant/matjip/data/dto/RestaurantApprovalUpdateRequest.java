package com.restaurant.matjip.data.dto;

import com.restaurant.matjip.data.domain.RestaurantApprovalStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RestaurantApprovalUpdateRequest {

    @NotNull(message = "Approval status is required")
    private RestaurantApprovalStatus status;

    @Size(max = 1000, message = "Rejected reason must be 1000 characters or fewer")
    private String rejectedReason;
}
