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

    private BlogType blogType;
    private String imageUrl;
    private int commentCount;

    public BlogDetailResponse(Blog blog, boolean recommended, int commentCount) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.content = blog.getContent();
        this.contentHtml = blog.getContentHtml();
        this.contentDelta = blog.getContentDelta();

        this.viewCount = blog.getViewCount();
        this.recommendCount = blog.getRecommendCount();
        this.recommended = recommended;

        this.createdAt = blog.getCreatedAt();

        this.authorId = blog.getUser().getId();
        this.authorNickname = blog.getUser().getNickname();

        this.blogType = blog.getBlogType();
        this.imageUrl = blog.getImageUrl();
        this.commentCount = commentCount;
    }
}
