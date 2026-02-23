package com.restaurant.matjip.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class RestaurantMyRequestUpdateRequest {

    @NotBlank(message = "Restaurant name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    private BigDecimal latitude;
    private BigDecimal longitude;
    private String phone;
    private String description;
    private String imageUrl;
    private String businessLicenseFileKey;
    private List<String> categoryNames;
}
