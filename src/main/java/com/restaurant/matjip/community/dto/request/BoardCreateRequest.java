package com.restaurant.matjip.community.dto.request;

import com.restaurant.matjip.community.domain.BoardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BoardCreateRequest {

    @NotBlank(message = "Please enter a title.")
    @Size(min = 2, message = "Title must be at least 2 characters.")
    private String title;

    @NotBlank(message = "Please enter content.")
    private String content;

    private String contentHtml;
    private String contentDelta;

    @NotNull(message = "Board type is required.")
    private BoardType boardType;
}
