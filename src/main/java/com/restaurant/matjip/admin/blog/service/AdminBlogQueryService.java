package com.restaurant.matjip.admin.blog.service;

import com.restaurant.matjip.blog.controller.enums.BlogSearchType;
import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.domain.BlogType;
import com.restaurant.matjip.blog.domain.BlogView;
import com.restaurant.matjip.blog.dto.response.BlogDetailResponse;
import com.restaurant.matjip.blog.dto.response.BlogListResponse;
import com.restaurant.matjip.blog.dto.response.BlogPageResponse;
import com.restaurant.matjip.admin.blog.repository.AdminBlogCommentRepository;
import com.restaurant.matjip.admin.blog.repository.AdminBlogRecommendationRepository;
import com.restaurant.matjip.admin.blog.repository.AdminBlogRepository;
import com.restaurant.matjip.admin.blog.repository.AdminBlogViewRepository;
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
public class AdminBlogQueryService {

    private final AdminBlogRepository blogRepository;
    private final AdminBlogViewRepository blogViewRepository;
    private final AdminBlogRecommendationRepository blogRecommendationRepository;
    private final AdminBlogCommentRepository commentRepository;

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
            case TITLE -> page = blogRepository.searchTitle(type, keyword, pageable);
            case CONTENT -> page = blogRepository.searchContent(type, keyword, pageable);
            case AUTHOR -> page = blogRepository.searchAuthor(type, keyword, pageable);
            case COMMENT -> page = blogRepository.searchComment(type, keyword, pageable);
            default -> page = blogRepository.searchNormalBlogs(type, keyword, pageable);
        }

        Page<BlogListResponse> normalPage =
                page.map(blog -> {
                    int count = commentRepository.countByBlogIdAndDeletedFalse(blog.getId());
                    return BlogListResponse.from(blog, count);
                });
        return new BlogPageResponse(notices, normalPage);
    }

    @Transactional
    public BlogDetailResponse getDetail(Long blogId, CustomUserDetails userDetails) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalStateException("blog not found"));

        if (userDetails == null) {
            blog.increaseViewCount();
            int commentCount = commentRepository.countByBlogIdAndDeletedFalse(blogId);
            return new BlogDetailResponse(blog, false, commentCount);
        }

        Long userId = userDetails.getId();
        if (!blogViewRepository.existsByBlogIdAndUserId(blogId, userId)) {
            BlogView view = BlogView.builder()
                    .blog(blog)
                    .userId(userId)
                    .viewedAt(LocalDateTime.now())
                    .build();
            blogViewRepository.save(view);
            blog.increaseViewCount();
        }

        boolean recommended = blogRecommendationRepository.existsByBlogIdAndUserId(blogId, userId);
        int commentCount = commentRepository.countByBlogIdAndDeletedFalse(blogId);
        return new BlogDetailResponse(blog, recommended, commentCount);
    }
}
