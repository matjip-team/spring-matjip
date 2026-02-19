package com.restaurant.matjip.blog.controller;

import com.restaurant.matjip.blog.dto.request.BlogImagePresignRequest;
import com.restaurant.matjip.blog.dto.response.BlogImagePresignResponse;
import com.restaurant.matjip.common.service.S3ImagePresignService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blogs/images")
@RequiredArgsConstructor
public class BlogImageController {

    private final S3ImagePresignService s3ImagePresignService;

    @PostMapping("/presigned-url")
    public ApiResponse<BlogImagePresignResponse> createPresignedUrl(
            @Valid @RequestBody BlogImagePresignRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        S3ImagePresignService.PresignedUploadResult presigned =
                s3ImagePresignService.createPresignedUpload(
                        "blogs",
                        request.getFileName(),
                        request.getContentType()
                );

        return ApiResponse.success(new BlogImagePresignResponse(
                presigned.getUploadUrl(),
                presigned.getFileUrl(),
                presigned.getMethod()
        ));
    }
}



