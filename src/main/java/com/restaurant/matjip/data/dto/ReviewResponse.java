package com.restaurant.matjip.data.dto;

import com.restaurant.matjip.data.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewResponse {

    private Long id;
    private String nickname;
    private int rating;
    private String content;
    private boolean isMine;

    /* 🔹 로그인 정보 없는 경우 (RestaurantService용) */
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getNickname(),
                review.getRating(),
                review.getContent(),
                false
        );
    }

    /* 🔹 로그인 정보 있는 경우 (ReviewController용) */
    public static ReviewResponse from(Review review, String currentUserEmail) {

        boolean mine = false;

        if (currentUserEmail != null &&
                review.getUser().getEmail().equals(currentUserEmail)) {
            mine = true;
        }

        return new ReviewResponse(
                review.getId(),
                review.getUser().getNickname(),
                review.getRating(),
                review.getContent(),
                mine
        );
    }
}
