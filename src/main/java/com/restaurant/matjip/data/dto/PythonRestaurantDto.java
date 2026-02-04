package com.restaurant.matjip.data.dto;

import lombok.Getter;

@Getter
public class PythonRestaurantDto {

    private String external_id;
    private String name;
    private double lat;
    private double lng;
    private String address;
    private String category;
    private String phone;
    private String source;
    private String image_url;
}
