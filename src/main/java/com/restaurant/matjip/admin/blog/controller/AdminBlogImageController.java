package com.restaurant.matjip.admin.blog.controller;

import com.restaurant.matjip.admin.blog.util.AdminAuthChecker;
import com.restaurant.matjip.blog.dto.request.BlogImagePresignRequest;
import com.restaurant.matjip.blog.dto.response.BlogImagePresignResponse;
import com.restaurant.matjip.common.service.S3ImagePresignService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/blogs/images")
@RequiredArgsConstructor
public class AdminBlogImageController {

    private final S3ImagePresignService s3ImagePresignService;

    @PostMapping("/presigned-url")
    public ApiResponse<BlogImagePresignResponse> createPresignedUrl(
            @Valid @RequestBody BlogImagePresignRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AdminAuthChecker.requireAdmin(userDetails);

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
