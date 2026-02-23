package com.restaurant.matjip.data.dto;

import com.restaurant.matjip.data.domain.Restaurant;
import com.restaurant.matjip.data.domain.RestaurantApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RestaurantMyRequestDTO {

    private Long id;
    private String name;
    private String address;
    private String imageUrl;
    private RestaurantApprovalStatus approvalStatus;
    private LocalDateTime createdAt;

    public static RestaurantMyRequestDTO from(Restaurant restaurant) {
        return from(restaurant, restaurant.getImageUrl());
    }

    public static RestaurantMyRequestDTO from(Restaurant restaurant, String imageUrl) {
        return new RestaurantMyRequestDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                imageUrl,
                restaurant.getApprovalStatus(),
                restaurant.getCreatedAt()
        );
    }
}
