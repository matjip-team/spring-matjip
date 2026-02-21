package com.restaurant.matjip.community.controller;

import com.restaurant.matjip.community.dto.request.CommentCreateRequest;
import com.restaurant.matjip.community.dto.request.CommentUpdateRequest;
import com.restaurant.matjip.community.dto.request.ReportCreateRequest;
import com.restaurant.matjip.community.dto.response.CommentResponse;
import com.restaurant.matjip.community.service.BoardReportService;
import com.restaurant.matjip.community.service.CommentService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final BoardReportService boardReportService;

    @PostMapping
    public ApiResponse<Void> create(
            @PathVariable Long boardId,
            @RequestBody CommentCreateRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }
        commentService.create(boardId, user.getId(), req);
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<List<CommentResponse>> list(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        return ApiResponse.success(commentService.getList(boardId, sort));
    }

    @PutMapping("/{commentId}")
    public ApiResponse<Void> update(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }
        commentService.update(commentId, user.getId(), req.getContent());
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> delete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }
        commentService.delete(commentId, user.getId());
        return ApiResponse.success(null);
    }

    @PostMapping("/{commentId}/reports")
    public ApiResponse<Void> reportComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @Valid @RequestBody ReportCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        boardReportService.reportComment(boardId, commentId, user.getId(), request);
        return ApiResponse.success("신고가 접수되었습니다.", null);
    }
}
