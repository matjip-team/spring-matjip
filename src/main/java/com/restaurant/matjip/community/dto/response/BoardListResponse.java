package com.restaurant.matjip.community.dto.response;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
public class BoardListResponse {

    private static final Pattern IMAGE_TAG_PATTERN =
            Pattern.compile("<img[\\s>]", Pattern.CASE_INSENSITIVE);
    private static final Pattern VIDEO_TAG_PATTERN =
            Pattern.compile("<(video|iframe)[\\s>]", Pattern.CASE_INSENSITIVE);

    private Long id;
    private BoardType boardType;
    private String title;
    private Long authorId;
    private String authorNickname;
    private int viewCount;
    private int recommendCount;
    private LocalDateTime createdAt;
    private int commentCount;
    private boolean hasImage;
    private boolean hasVideo;
    private boolean hidden;
    private int reportCount;

    public static BoardListResponse from(Board board, int commentCount) {
        String content = board.getContent() == null ? "" : board.getContent();
        boolean hasImage = hasImage(board, content);
        boolean hasVideo = hasVideo(content);

        return new BoardListResponse(
                board.getId(),
                board.getBoardType(),
                board.getTitle(),
                board.getUser().getId(),
                board.getUser().getNickname(),
                board.getViewCount(),
                board.getRecommendCount(),
                board.getCreatedAt(),
                commentCount,
                hasImage,
                hasVideo,
                board.isHidden(),
                board.getReportCount()
        );
    }

    private static boolean hasImage(Board board, String content) {
        return hasImageUrl(board) || IMAGE_TAG_PATTERN.matcher(content).find();
    }

    private static boolean hasImageUrl(Board board) {
        String imageUrl = board.getImageUrl();
        return imageUrl != null && !imageUrl.isBlank();
    }

    private static boolean hasVideo(String content) {
        return VIDEO_TAG_PATTERN.matcher(content).find();
    }
}
