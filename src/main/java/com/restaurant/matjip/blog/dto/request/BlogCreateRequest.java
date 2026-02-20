package com.restaurant.matjip.blog.dto.request;

import com.restaurant.matjip.blog.domain.BlogType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BlogCreateRequest {

    @NotBlank(message = "Please enter a title.")
    @Size(min = 2, message = "Title must be at least 2 characters.")
    private String title;

    @NotBlank(message = "Please enter content.")
    private String content;

    private String contentHtml;
    private String contentDelta;

    @NotNull(message = "Blog type is required.")
    private BlogType blogType;

    private String imageUrl;
}
