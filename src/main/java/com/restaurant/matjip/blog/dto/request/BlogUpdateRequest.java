package com.restaurant.matjip.blog.dto.request;

import com.restaurant.matjip.blog.domain.BlogType;
import lombok.Getter;

@Getter
public class BlogUpdateRequest {

    private String title;
    private String content;

    private String contentHtml;
    private String contentDelta;
    private String imageUrl;
    private BlogType boardType;
}




