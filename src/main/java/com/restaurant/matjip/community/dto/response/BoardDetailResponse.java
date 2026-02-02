package com.restaurant.matjip.community.dto.response;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.BoardType;
import lombok.Getter;

@Getter
public class BoardDetailResponse {

    private Long id;
    private String title;
    private String content;
    private int viewCount;
    private int recommendCount;

    private Long authorId;
    private String authorNickname;

    private BoardType boardType;

    public BoardDetailResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.viewCount = board.getViewCount();
        this.recommendCount = board.getRecommendCount();
        this.authorId = board.getUser().getId();
        this.authorNickname = board.getUser().getNickname();
        this.boardType = board.getBoardType();
    }
}

