package com.restaurant.matjip.community.dto.response;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.BoardType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardDetailResponse {

    /* ================== 게시글 기본 정보 ================== */

    private Long id;
    private String title;
    private String content;

    private int viewCount;
    private int recommendCount;
    private  boolean recommended;

    private LocalDateTime createdAt;

    /* ================== 작성자 정보 ================== */

    private Long authorId;
    private String authorNickname;

    /* ================== 게시글 속성 ================== */

    private BoardType boardType;
    private String imageUrl;
    private int commentCount;

    public BoardDetailResponse(Board board, boolean recommended, int commentCount) {

        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();

        this.viewCount = board.getViewCount();
        this.recommendCount = board.getRecommendCount();
        this.recommended = recommended;

        this.createdAt = board.getCreatedAt();

        this.authorId = board.getUser().getId();
        this.authorNickname = board.getUser().getNickname();

        this.boardType = board.getBoardType();
        this.imageUrl = board.getImageUrl();

        this.commentCount = commentCount;
    }
}

