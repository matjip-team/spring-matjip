package com.restaurant.matjip.blog.dto.request;

import lombok.Getter;

@Getter
public class BlogCommentCreateRequest {

    private String content;

    // 대댓글일 경우 부모 댓글 id
    private Long parentId;
}


