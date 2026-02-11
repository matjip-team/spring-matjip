package com.restaurant.matjip.community.dto.request;

import lombok.Getter;

@Getter
public class CommentCreateRequest {

    private String content;

    // 대댓글일 경우 부모 댓글 id
    private Long parentId;
}
