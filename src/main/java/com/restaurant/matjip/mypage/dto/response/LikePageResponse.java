package com.restaurant.matjip.mypage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikePageResponse {

    private List<LikeResponse> likes;
    private Long nextCursor;

    public static LikePageResponse from(List<LikeResponse> likeResponse, Long nextCursor) {
        return new LikePageResponse(
                likeResponse,
                nextCursor
        );
    }
}
