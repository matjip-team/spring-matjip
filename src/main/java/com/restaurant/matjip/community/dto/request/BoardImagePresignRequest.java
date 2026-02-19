package com.restaurant.matjip.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class BoardImagePresignRequest {

    @NotBlank(message = "파일명을 입력해 주세요.")
    private String fileName;

    @NotBlank(message = "콘텐츠 타입을 입력해 주세요.")
    @Pattern(regexp = "^(image|video)\\/.*$", message = "이미지/영상 파일만 업로드할 수 있습니다.")
    private String contentType;
}
