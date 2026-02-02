package com.restaurant.matjip.community.dto.request;

import com.restaurant.matjip.community.domain.BoardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 게시글 생성 요청 DTO
 */
@Getter
public class BoardCreateRequest {

    @NotBlank
    private String title;      // 제목

    @NotBlank
    private String content;    // 내용

    @NotNull
    private BoardType boardType; // NOTICE / REVIEW
}
