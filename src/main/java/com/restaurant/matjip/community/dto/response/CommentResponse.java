package com.restaurant.matjip.community.dto.response;

import com.restaurant.matjip.community.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentResponse {

    private Long id;
    private Long authorId;
    private String content;
    private String authorNickname;
    private LocalDateTime createdAt;
    private List<CommentResponse> children;
    private boolean deleted;
    private int reportCount;

    public static CommentResponse from(Comment c) {
        return CommentResponse.builder()
                .id(c.getId())
                .authorId(c.getUser().getId())
                .content(c.getContent())
                .authorNickname(c.getUser().getNickname())
                .createdAt(c.getCreatedAt())
                .deleted(c.isDeleted())
                .reportCount(c.getReportCount())
                .children(
                        c.getChildren() == null
                                ? List.of()
                                : c.getChildren()
                                .stream()
                                .map(CommentResponse::from)
                                .toList()
                )
                .build();
    }
}
