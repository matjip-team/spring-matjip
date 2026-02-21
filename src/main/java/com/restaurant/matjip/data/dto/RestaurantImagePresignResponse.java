package com.restaurant.matjip.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantImagePresignResponse {
    private String uploadUrl;
    private String fileUrl;
    private String method;
}
