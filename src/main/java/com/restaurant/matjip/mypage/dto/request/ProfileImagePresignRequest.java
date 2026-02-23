package com.restaurant.matjip.mypage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ProfileImagePresignRequest {

    @NotBlank(message = "fileName is required")
    private String fileName;

    @NotBlank(message = "contentType is required")
    @Pattern(regexp = "^image\\/.*$", message = "Only image files are allowed")
    private String contentType;
}
