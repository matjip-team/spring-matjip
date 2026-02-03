package com.restaurant.matjip.mypage.dto.response;

import com.restaurant.matjip.data.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private byte rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long restaurantId;
    private String restaurantName;

    public static ReviewResponse from(Review r) {
        return new ReviewResponse(
                r.getId(),
                r.getRating(),
                r.getContent(),
                r.getCreatedAt(),
                r.getUpdatedAt(),
                r.getRestaurant().getId(),
                r.getRestaurant().getName()
        );
    }

}
