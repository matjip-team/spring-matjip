package com.restaurant.matjip.data.dto;

import com.restaurant.matjip.data.domain.Restaurant;
import com.restaurant.matjip.data.domain.RestaurantApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RestaurantAdminListDTO {

    private Long id;
    private String name;
    private String address;
    private String imageUrl;
    private boolean hasBusinessLicenseFile;
    private RestaurantApprovalStatus approvalStatus;
    private LocalDateTime createdAt;

    public static RestaurantAdminListDTO from(Restaurant restaurant) {
        return from(restaurant, restaurant.getImageUrl());
    }

    public static RestaurantAdminListDTO from(Restaurant restaurant, String imageUrl) {
        return new RestaurantAdminListDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                imageUrl,
                restaurant.getBusinessLicenseFileUrl() != null && !restaurant.getBusinessLicenseFileUrl().isBlank(),
                restaurant.getApprovalStatus(),
                restaurant.getCreatedAt()
        );
    }
}
