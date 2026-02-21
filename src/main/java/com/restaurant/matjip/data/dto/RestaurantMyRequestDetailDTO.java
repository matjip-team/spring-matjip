package com.restaurant.matjip.data.dto;

import com.restaurant.matjip.data.domain.Restaurant;
import com.restaurant.matjip.data.domain.RestaurantApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantMyRequestDetailDTO {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String description;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<String> categoryNames;
    private boolean hasBusinessLicenseFile;
    private RestaurantApprovalStatus approvalStatus;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
    private String rejectedReason;

    public static RestaurantMyRequestDetailDTO from(Restaurant restaurant) {
        return RestaurantMyRequestDetailDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .phone(restaurant.getPhone())
                .description(restaurant.getDescription())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .categoryNames(
                        restaurant.getCategories()
                                .stream()
                                .map(category -> category.getName())
                                .sorted()
                                .toList()
                )
                .hasBusinessLicenseFile(
                        restaurant.getBusinessLicenseFileUrl() != null
                                && !restaurant.getBusinessLicenseFileUrl().isBlank()
                )
                .approvalStatus(restaurant.getApprovalStatus())
                .createdAt(restaurant.getCreatedAt())
                .reviewedAt(
                        restaurant.getApprovalStatus() == RestaurantApprovalStatus.PENDING
                                ? null
                                : restaurant.getUpdatedAt()
                )
                .rejectedReason(restaurant.getRejectedReason())
                .build();
    }
}
