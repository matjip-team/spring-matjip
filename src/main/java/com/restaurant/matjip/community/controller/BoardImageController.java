package com.restaurant.matjip.community.controller;

import com.restaurant.matjip.community.dto.request.BoardImagePresignRequest;
import com.restaurant.matjip.community.dto.response.BoardImagePresignResponse;
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
@RequestMapping("/api/boards/images")
@RequiredArgsConstructor
public class BoardImageController {

    private final S3ImagePresignService s3ImagePresignService;

    @PostMapping("/presigned-url")
    public ApiResponse<BoardImagePresignResponse> createPresignedUrl(
            @Valid @RequestBody BoardImagePresignRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        S3ImagePresignService.PresignedUploadResult presigned =
                s3ImagePresignService.createPresignedUpload(
                        "boards",
                        request.getFileName(),
                        request.getContentType()
                );

        return ApiResponse.success(new BoardImagePresignResponse(
                presigned.getUploadUrl(),
                presigned.getFileUrl(),
                presigned.getMethod()
        ));
    }
}
