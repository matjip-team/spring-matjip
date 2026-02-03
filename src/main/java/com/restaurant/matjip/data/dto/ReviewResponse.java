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

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getNickname(),
                review.getRating(),
                review.getContent()
        );
    }
}
