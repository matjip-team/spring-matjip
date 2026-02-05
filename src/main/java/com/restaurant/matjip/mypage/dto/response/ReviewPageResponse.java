package com.restaurant.matjip.mypage.dto.response;

import com.restaurant.matjip.data.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewPageResponse {

    private List<ReviewResponse> reviews;
    private Long nextCursor;

    public static ReviewPageResponse from(List<Review> reviews, Long nextCursor) {
        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(ReviewResponse::from) // Review → ReviewResponse 변환
                .toList(); // Java 16 이상
        return new ReviewPageResponse(
                reviewResponses,
                nextCursor
        );
    }

}
