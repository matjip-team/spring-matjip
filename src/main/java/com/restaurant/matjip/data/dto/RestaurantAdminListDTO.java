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
    private boolean hasBusinessLicenseFile;
    private RestaurantApprovalStatus approvalStatus;
    private LocalDateTime createdAt;

    public static RestaurantAdminListDTO from(Restaurant restaurant) {
        return new RestaurantAdminListDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getBusinessLicenseFileUrl() != null && !restaurant.getBusinessLicenseFileUrl().isBlank(),
                restaurant.getApprovalStatus(),
                restaurant.getCreatedAt()
        );
    }
}
