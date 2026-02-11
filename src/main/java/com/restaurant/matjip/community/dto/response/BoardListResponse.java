package com.restaurant.matjip.community.dto.response;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardListResponse {

    private Long id;                 // 게시글 ID
    private BoardType boardType;     // 말머리 (NOTICE / REVIEW)
    private String title;            // 제목
    private String authorNickname;   // 작성자 닉네임
    private int viewCount;           // 조회수
    private int recommendCount;      // 추천 수
    private LocalDateTime createdAt; // 작성일
    private int commentCount; // 댓글 수

    public static BoardListResponse from(Board board, int commentCount) {
        return new BoardListResponse(
                board.getId(),
                board.getBoardType(),
                board.getTitle(),
                board.getUser().getNickname(),
                board.getViewCount(),
                board.getRecommendCount(),
                board.getCreatedAt(),
                commentCount
        );
    }
}
