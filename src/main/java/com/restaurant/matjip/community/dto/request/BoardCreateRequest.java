package com.restaurant.matjip.community.dto.request;

import com.restaurant.matjip.community.domain.BoardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 게시글 생성 요청 DTO
 */
@Getter
public class BoardCreateRequest {

    @NotBlank(message = "제목을 입력하십시오.")
    @Size(min = 2, message = "제목은 최소 2자 이상 입력해 주십시오.")
    private String title;      // 제목

    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;    // 내용

    @NotNull(message = "게시글 타입이 없습니다.")
    private BoardType boardType; // NOTICE / REVIEW

}
