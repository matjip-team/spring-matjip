package com.restaurant.matjip.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class RestaurantSearchRequest {

    // ?categories=한식,카페
    private List<String> categories;

    public void setCategories(String categories) {
        if (categories == null || categories.isBlank()) {
            this.categories = null;
            return;
        }
        this.categories = Arrays.stream(categories.split(","))
                .map(String::trim)
                .toList();
    }
}
