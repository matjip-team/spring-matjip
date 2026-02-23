package com.restaurant.matjip.blog.service;

import com.restaurant.matjip.blog.controller.enums.BlogSearchType;
import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.domain.BlogType;
import com.restaurant.matjip.blog.domain.BlogView;
import com.restaurant.matjip.blog.dto.response.BlogDetailResponse;
import com.restaurant.matjip.blog.dto.response.BlogListResponse;
import com.restaurant.matjip.blog.dto.response.BlogPageResponse;
import com.restaurant.matjip.blog.repository.BlogRecommendationRepository;
import com.restaurant.matjip.blog.repository.BlogRepository;
import com.restaurant.matjip.blog.repository.BlogViewRepository;
import com.restaurant.matjip.blog.repository.BlogCommentRepository;
import com.restaurant.matjip.global.common.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogQueryService {

    private final BlogRepository blogRepository;
    private final BlogViewRepository blogViewRepository;
    private final BlogRecommendationRepository blogRecommendationRepository;
    private final BlogCommentRepository commentRepository;

    /* ================== 게시글 목록 조회 ================== */

    @Transactional(readOnly = true)
    public BlogPageResponse getBlogs(
            BlogType type,
            String keyword,
            BlogSearchType searchType,
            Pageable pageable
    ) {

        List<BlogListResponse> notices =
                blogRepository.findByBlogTypeOrderByIdDesc(BlogType.NOTICE)
                        .stream()
                        .map(blog -> {
                            int count = commentRepository.countByBlogIdAndDeletedFalse(blog.getId());
                            return BlogListResponse.from(blog, count);
                        })
                        .toList();

        Page<Blog> page;

        switch (searchType) {
            case TITLE ->
                    page = blogRepository.searchTitle(type, keyword, pageable);

            case CONTENT ->
                    page = blogRepository.searchContent(type, keyword, pageable);

            case AUTHOR ->
                    page = blogRepository.searchAuthor(type, keyword, pageable);

            case COMMENT ->
                    page = blogRepository.searchComment(type, keyword, pageable);

            default ->
                    page = blogRepository.searchNormalBlogs(type, keyword, pageable);
        }

        Page<BlogListResponse> normalPage =
                page.map(blog -> {
                    int count = commentRepository.countByBlogIdAndDeletedFalse(blog.getId());
                    return BlogListResponse.from(blog, count);
                });
        return new BlogPageResponse(notices, normalPage);
    }


    /* ================== 게시글 상세 조회 + 조회수 처리 ================== */

    @Transactional
    public BlogDetailResponse getDetail(Long blogId, CustomUserDetails userDetails) {

        // 게시글 조회
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow();

        // 🔹 비로그인 유저 → 그냥 조회수 증가
        if (userDetails == null) {
            blog.increaseViewCount();
            int commentCount = commentRepository.countByBlogIdAndDeletedFalse(blogId);
            return new BlogDetailResponse(blog, false, commentCount);
        }

        Long userId = userDetails.getId();

        // 🔹 로그인 유저 + 처음 보는 글일 때만 조회수 증가
        if (!blogViewRepository.existsByBlogIdAndUserId(blogId, userId)) {

            BlogView view = BlogView.builder()
                    .blog(blog)
                    .userId(userId)
                    .viewedAt(LocalDateTime.now())
                    .build();

            blogViewRepository.save(view);
            blog.increaseViewCount();
        }

        boolean recommended =
                blogRecommendationRepository.existsByBlogIdAndUserId(blogId, userId);

        int commentCount = commentRepository.countByBlogIdAndDeletedFalse(blogId);
        return new BlogDetailResponse(blog, recommended, commentCount);
    }
}



