package com.restaurant.matjip.admin.blog.controller;

import com.restaurant.matjip.admin.blog.service.AdminBlogCommentService;
import com.restaurant.matjip.admin.blog.util.AdminAuthChecker;
import com.restaurant.matjip.blog.dto.request.BlogCommentCreateRequest;
import com.restaurant.matjip.blog.dto.request.BlogCommentUpdateRequest;
import com.restaurant.matjip.blog.dto.response.BlogCommentResponse;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/blogs/{blogId}/comments")
@RequiredArgsConstructor
public class AdminBlogCommentController {

    private final AdminBlogCommentService commentService;

    @PostMapping
    public ApiResponse<Void> create(
            @PathVariable Long blogId,
            @RequestBody BlogCommentCreateRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        AdminAuthChecker.requireAdmin(user);
        commentService.create(blogId, user.getId(), req);
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<List<BlogCommentResponse>> list(
            @PathVariable Long blogId,
            @RequestParam(defaultValue = "latest") String sort,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        AdminAuthChecker.requireAdmin(user);
        return ApiResponse.success(commentService.getList(blogId, sort));
    }

    @PutMapping("/{commentId}")
    public ApiResponse<Void> update(
            @PathVariable Long blogId,
            @PathVariable Long commentId,
            @RequestBody BlogCommentUpdateRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        AdminAuthChecker.requireAdmin(user);
        commentService.update(commentId, user.getId(), req.getContent());
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> delete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        AdminAuthChecker.requireAdmin(user);
        commentService.delete(commentId, user.getId());
        return ApiResponse.success(null);
    }
}
