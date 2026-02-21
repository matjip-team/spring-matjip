package com.restaurant.matjip.mypage.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private long rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long restaurantId;
    private String restaurantName;
    private String address;
    private String imageUrl;

    private double avgRating;
    private long reviewCount;

    private List<CategroyResponse> categories = new ArrayList<>();

    // JPQL 생성자용 응답객체에는 미포함
    @JsonIgnore
    private Long categoryId;
    @JsonIgnore
    private String categoryName;

    public ReviewResponse(Long id, long rating, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Long restaurantId, String restaurantName, String address, String imageUrl, double avgRating, long reviewCount, Long categoryId, String categoryName) {
        this.id = id;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.address = address;
        this.imageUrl = imageUrl;
        this.avgRating = avgRating;
        this.reviewCount = reviewCount;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
