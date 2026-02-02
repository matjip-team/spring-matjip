package com.restaurant.matjip.community.dto.internal;

import com.restaurant.matjip.community.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private Long id;
    private String title;
    private String content;
    private int viewCount;
    private Long userId;

    public static BoardDTO fromEntity(Board board) {
        return new BoardDTO(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getViewCount(),
                board.getUser().getId()
        );
    }
}
