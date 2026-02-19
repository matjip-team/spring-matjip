package com.restaurant.matjip.mypage.controller;

import com.restaurant.matjip.common.service.S3ImagePresignService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import com.restaurant.matjip.mypage.dto.request.ProfileImagePresignRequest;
import com.restaurant.matjip.mypage.dto.response.ProfileImagePresignResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mypage/profile-images")
@RequiredArgsConstructor
public class ProfileImageController {

    private final S3ImagePresignService s3ImagePresignService;

    @PostMapping("/presigned-url")
    public ApiResponse<ProfileImagePresignResponse> createPresignedUrl(
            @Valid @RequestBody ProfileImagePresignRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        S3ImagePresignService.PresignedUploadResult presigned =
                s3ImagePresignService.createPresignedUpload(
                        "profiles",
                        request.getFileName(),
                        request.getContentType()
                );

        return ApiResponse.success(new ProfileImagePresignResponse(
                presigned.getUploadUrl(),
                presigned.getFileUrl(),
                presigned.getMethod()
        ));
    }
}
