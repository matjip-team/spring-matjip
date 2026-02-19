package com.restaurant.matjip.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantLicensePresignResponse {

    private String uploadUrl;
    private String fileKey;
    private String method;
}
