package com.restaurant.matjip.admin.blog.controller;

import com.restaurant.matjip.admin.blog.service.AdminBlogCommandService;
import com.restaurant.matjip.admin.blog.service.AdminBlogQueryService;
import com.restaurant.matjip.admin.blog.service.AdminBlogRecommendationService;
import com.restaurant.matjip.admin.blog.util.AdminAuthChecker;
import com.restaurant.matjip.blog.controller.enums.BlogSearchType;
import com.restaurant.matjip.blog.domain.BlogType;
import com.restaurant.matjip.blog.dto.request.BlogCreateRequest;
import com.restaurant.matjip.blog.dto.request.BlogUpdateRequest;
import com.restaurant.matjip.blog.dto.response.BlogDetailResponse;
import com.restaurant.matjip.blog.dto.response.BlogPageResponse;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/blogs")
@RequiredArgsConstructor
public class AdminBlogController {

    private final AdminBlogQueryService blogQueryService;
    private final AdminBlogRecommendationService blogRecommendationService;
    private final AdminBlogCommandService blogCommandService;

    @GetMapping
    public ApiResponse<BlogPageResponse> getBlogs(
            @RequestParam(required = false) BlogType type,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "TITLE_CONTENT") BlogSearchType searchType,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AdminAuthChecker.requireAdmin(userDetails);
        return ApiResponse.success(blogQueryService.getBlogs(type, keyword, searchType, pageable));
    }

    @PostMapping
    public ApiResponse<Long> createBlog(
            @Valid @RequestBody BlogCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AdminAuthChecker.requireAdmin(userDetails);
        Long blogId = blogCommandService.create(request, userDetails.getId());
        return ApiResponse.success(blogId);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateBlog(
            @PathVariable Long id,
            @RequestBody BlogUpdateRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        AdminAuthChecker.requireAdmin(user);
        blogCommandService.update(id, user.getId(), req);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBlog(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AdminAuthChecker.requireAdmin(userDetails);
        blogCommandService.delete(id, userDetails.getId());
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/recommendations")
    public ApiResponse<Void> recommend(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AdminAuthChecker.requireAdmin(userDetails);
        blogRecommendationService.toggleRecommend(id, userDetails.getId());
        return ApiResponse.success(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<BlogDetailResponse> getBlogDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AdminAuthChecker.requireAdmin(userDetails);
        return ApiResponse.success(blogQueryService.getDetail(id, userDetails));
    }
}
