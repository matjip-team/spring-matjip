package com.restaurant.matjip.blog.dto.response;

import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.domain.BlogType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BlogDetailResponse {

    private Long id;
    private String title;
    private String content;
    private String contentHtml;
    private String contentDelta;

    private int viewCount;
    private int recommendCount;
    private boolean recommended;

    private LocalDateTime createdAt;

    private Long authorId;
    private String authorNickname;

    private BlogType boardType;
    private String imageUrl;
    private int commentCount;

    public BlogDetailResponse(Blog board, boolean recommended, int commentCount) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.contentHtml = board.getContentHtml();
        this.contentDelta = board.getContentDelta();

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
