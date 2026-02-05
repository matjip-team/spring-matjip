package com.restaurant.matjip.mypage.dto.response;

import com.restaurant.matjip.data.domain.RestaurantLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeResponse {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long restaurantId;
    private String restaurantName;

    private double avgRating;
    private long reviewCount;

    public static LikeResponse from(RestaurantLike r) {
        return new LikeResponse(
                r.getId(),
                r.getCreatedAt(),
                r.getUpdatedAt(),
                r.getRestaurant().getId(),
                r.getRestaurant().getName(),
                0,
                0
        );
    }
}
