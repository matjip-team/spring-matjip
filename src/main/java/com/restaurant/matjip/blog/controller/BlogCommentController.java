package com.restaurant.matjip.blog.controller;

import com.restaurant.matjip.blog.dto.request.BlogCommentCreateRequest;
import com.restaurant.matjip.blog.dto.request.BlogCommentUpdateRequest;
import com.restaurant.matjip.blog.dto.response.BlogCommentResponse;
import com.restaurant.matjip.blog.service.BlogCommentService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blogs/{boardId}/comments")
public class BlogCommentController {

    private final BlogCommentService commentService;

    /* 작성 */
    @PostMapping
    public ApiResponse<Void> create(
            @PathVariable Long boardId,
            @RequestBody BlogCommentCreateRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        commentService.create(boardId, user.getId(), req);
        return ApiResponse.success(null);
    }

    /* 목록 정렬 */
    @GetMapping
    public ApiResponse<List<BlogCommentResponse>> list(
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
            @RequestBody BlogCommentUpdateRequest req,
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




