package com.restaurant.matjip.community.controller;

import com.restaurant.matjip.community.dto.request.CommentCreateRequest;
import com.restaurant.matjip.community.dto.request.CommentUpdateRequest;
import com.restaurant.matjip.community.dto.response.CommentResponse;
import com.restaurant.matjip.community.service.CommentService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/comments")
public class CommentController {

    private final CommentService commentService;

    /* 작성 */
    @PostMapping
    public ApiResponse<Void> create(
            @PathVariable Long boardId,
            @RequestBody CommentCreateRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        commentService.create(boardId, user.getId(), req);
        return ApiResponse.success(null);
    }

    /* 목록 정렬 */
    @GetMapping
    public ApiResponse<List<CommentResponse>> list(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        return ApiResponse.success(commentService.getList(boardId, sort));
    }

    /* 수정 */
    @PutMapping("/{commentId}")
    public ApiResponse<Void> update(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        commentService.update(commentId, user.getId(), req.getContent());
        return ApiResponse.success(null);
    }

    /* 삭제 */
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> delete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        commentService.delete(commentId, user.getId());
        return ApiResponse.success(null);
    }
}

