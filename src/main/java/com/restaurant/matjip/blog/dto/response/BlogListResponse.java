package com.restaurant.matjip.blog.dto.response;

import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.domain.BlogType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
public class BlogListResponse {

    private static final Pattern IMAGE_TAG_PATTERN =
            Pattern.compile("<img[\\s>]", Pattern.CASE_INSENSITIVE);
    private static final Pattern VIDEO_TAG_PATTERN =
            Pattern.compile("<(video|iframe)[\\s>]", Pattern.CASE_INSENSITIVE);

    private Long id;
    private BlogType blogType;
    private String title;
    private String authorNickname;
    private int viewCount;
    private int recommendCount;
    private LocalDateTime createdAt;
    private int commentCount;
    private String imageUrl;
    private boolean hasImage;
    private boolean hasVideo;

    public static BlogListResponse from(Blog blog, int commentCount) {
        String content = blog.getContent() == null ? "" : blog.getContent();
        boolean hasImage = hasImage(blog, content);
        boolean hasVideo = hasVideo(content);

        return new BlogListResponse(
                blog.getId(),
                blog.getBlogType(),
                blog.getTitle(),
                blog.getUser().getNickname(),
                blog.getViewCount(),
                blog.getRecommendCount(),
                blog.getCreatedAt(),
                commentCount,
                blog.getImageUrl(),
                hasImage,
                hasVideo
        );
    }

    private static boolean hasImage(Blog blog, String content) {
        return hasImageUrl(blog) || IMAGE_TAG_PATTERN.matcher(content).find();
    }

    private static boolean hasImageUrl(Blog blog) {
        String imageUrl = blog.getImageUrl();
        return imageUrl != null && !imageUrl.isBlank();
    }

    private static boolean hasVideo(String content) {
        return VIDEO_TAG_PATTERN.matcher(content).find();
    }
}


