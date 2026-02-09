package com.restaurant.matjip.mypage.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurant.matjip.data.domain.RestaurantLike;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    private List<CategroyResponse> categories = new ArrayList<>();

    // JPQL 생성자용 응답객체에는 미포함
    @JsonIgnore
    private Long categoryId;
    @JsonIgnore
    private String categoryName;    

    public LikeResponse(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Long restaurantId, String restaurantName, double avgRating, long reviewCount, Long categoryId, String categoryName) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.avgRating = avgRating;
        this.reviewCount = reviewCount;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public LikeResponse(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Long restaurantId, String restaurantName) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }

    public static LikeResponse from(RestaurantLike r) {
        return new LikeResponse(
                r.getId(),
                r.getCreatedAt(),
                r.getUpdatedAt(),
                r.getRestaurant().getId(),
                r.getRestaurant().getName()
        );
    }
}
