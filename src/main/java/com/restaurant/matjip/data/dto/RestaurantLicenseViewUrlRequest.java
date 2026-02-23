package com.restaurant.matjip.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantLicenseViewUrlRequest {

    @NotBlank(message = "fileKey is required")
    private String fileKey;
}

