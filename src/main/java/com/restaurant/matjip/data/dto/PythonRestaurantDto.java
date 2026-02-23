package com.restaurant.matjip.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PythonRestaurantDto {

    @JsonProperty("external_id")
    private String externalId;

    private String name;
    private double lat;
    private double lng;
    private String address;
    private String category;
    private String phone;
    private String source;
    private String description;

    @JsonProperty("image_url")
    private String imageUrl;
}
