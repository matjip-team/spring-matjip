package com.restaurant.matjip.blog.controller;

import com.restaurant.matjip.blog.domain.BlogType;
import com.restaurant.matjip.blog.dto.request.BlogCreateRequest;
import com.restaurant.matjip.blog.controller.enums.BlogSearchType;
import com.restaurant.matjip.blog.dto.request.BlogUpdateRequest;
import com.restaurant.matjip.blog.dto.response.BlogDetailResponse;
import com.restaurant.matjip.blog.dto.response.BlogPageResponse;
import com.restaurant.matjip.blog.service.BlogCommandService;
import com.restaurant.matjip.blog.service.BlogQueryService;
import com.restaurant.matjip.blog.service.BlogRecommendationService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogQueryService blogQueryService;
    private final BlogRecommendationService blogRecommendationService;
    private final BlogCommandService blogCommandService;

    /* ===================== 게시글 검색 ===================== */

    @GetMapping
    public ApiResponse<BlogPageResponse> getBlogs(
            @RequestParam(required = false) BlogType type,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "TITLE_CONTENT") BlogSearchType searchType,
            Pageable pageable
    ) {
        return ApiResponse.success(blogQueryService.getBlogs(type, keyword, searchType, pageable));
    }

    /* ===================== 게시글 작성 ===================== */

    @PostMapping
    public ApiResponse<Long> createBlog(
            @Valid @RequestBody BlogCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        Long blogId = blogCommandService.create(request, userDetails.getId());
        return ApiResponse.success(blogId);
    }

    /* ===================== 게시글 수정 ===================== */
    @PutMapping("/{id}")
    public ApiResponse<Void> updateBlog(
            @PathVariable Long id,
            @RequestBody BlogUpdateRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        blogCommandService.update(id, user.getId(), req);
        return ApiResponse.success(null);
    }

    /* ===================== 게시글 삭제 ===================== */

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBlog(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        blogCommandService.delete(id, userDetails.getId());
        return ApiResponse.success(null);
    }

    /* ===================== 추천 ===================== */

    @PostMapping("/{id}/recommendations")
    public ApiResponse<Void> recommend(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        blogRecommendationService.toggleRecommend(id, userDetails.getId());
        return ApiResponse.success(null);
    }

    /* ===================== 게시글 상세 ===================== */

    @GetMapping("/{id}")
    public ApiResponse<BlogDetailResponse> getBlogDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.success(
                blogQueryService.getDetail(id, userDetails)
        );
    }
}



