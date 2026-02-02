package com.restaurant.matjip.mypage.dto.response;

import com.restaurant.matjip.community.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private byte rating;
    private String content;

    public static ReviewResponse from(Review r) {
        return new ReviewResponse(
                r.getId(),
                r.getRating(),
                r.getContent()
        );
    }

}
