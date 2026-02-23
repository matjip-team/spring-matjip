package com.restaurant.matjip.blog.dto.response;

import com.restaurant.matjip.blog.domain.BlogComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BlogCommentResponse {

    private Long id;
    private String content;
    private String authorNickname;
    private LocalDateTime createdAt;
    private List<BlogCommentResponse> children;
    private boolean deleted;

    public static BlogCommentResponse from(BlogComment c) {

        return BlogCommentResponse.builder()
                .id(c.getId())
                .content(c.getContent())
                .authorNickname(c.getUser().getNickname())
                .createdAt(c.getCreatedAt())
                .deleted(c.isDeleted())
                .children(
                        c.getChildren() == null
                                ? List.of()
                                : c.getChildren()
                                .stream()
                                .map(BlogCommentResponse::from)
                                .toList()
                )
                .build();
    }
}


