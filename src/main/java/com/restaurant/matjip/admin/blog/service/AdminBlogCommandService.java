package com.restaurant.matjip.admin.blog.service;

import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.dto.request.BlogCreateRequest;
import com.restaurant.matjip.blog.dto.request.BlogUpdateRequest;
import com.restaurant.matjip.admin.blog.repository.AdminBlogRepository;
import com.restaurant.matjip.admin.blog.repository.AdminBlogViewRepository;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 관리자 블로그 명령 서비스
 * - ADMIN 역할: 모든 사용자의 블로그 수정/삭제 가능 (ownership 체크 없음)
 */
@Service
@RequiredArgsConstructor
public class AdminBlogCommandService {

    private final AdminBlogRepository blogRepository;
    private final UserRepository userRepository;
    private final AdminBlogViewRepository blogViewRepository;

    @Transactional
    public Long create(BlogCreateRequest request, Long userId) {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("user not found"));

        Blog blog = Blog.builder()
                .title(request.getTitle())
                .content(resolvePrimaryContent(request.getContent(), request.getContentHtml(), null))
                .contentHtml(resolveHtmlContent(request.getContentHtml(), request.getContent(), null))
                .contentDelta(blankToNull(request.getContentDelta()))
                .blogType(request.getBlogType())
                .imageUrl(blankToNull(request.getImageUrl()))
                .user(writer)
                .viewCount(0)
                .recommendCount(0)
                .build();

        return blogRepository.save(blog).getId();
    }

    /**
     * 관리자 슈퍼권한: 작성자 여부 체크 없이 수정 가능
     */
    @Transactional
    public void update(Long blogId, Long adminUserId, BlogUpdateRequest req) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalStateException("blog not found"));
        // ADMIN은 ownership 체크 생략

        blog.setTitle(req.getTitle());
        blog.setContent(resolvePrimaryContent(req.getContent(), req.getContentHtml(), blog.getContent()));
        blog.setContentHtml(resolveHtmlContent(req.getContentHtml(), req.getContent(), blog.getContentHtml()));
        blog.setContentDelta(blankToNull(req.getContentDelta()));
        blog.setImageUrl(blankToNull(req.getImageUrl()));
        blog.setBlogType(req.getBlogType());
    }

    /**
     * 관리자 슈퍼권한: 작성자 여부 체크 없이 삭제 가능
     */
    @Transactional
    public void delete(Long blogId, Long adminUserId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalStateException("blog not found"));
        // ADMIN은 ownership 체크 생략

        blogViewRepository.deleteByBlogId(blogId);
        blogRepository.delete(blog);
    }

    private String resolvePrimaryContent(String content, String contentHtml, String fallback) {
        if (hasText(content)) return content;
        if (hasText(contentHtml)) return contentHtml;
        return fallback;
    }

    private String resolveHtmlContent(String contentHtml, String content, String fallback) {
        if (hasText(contentHtml)) return contentHtml;
        if (hasText(content)) return content;
        return fallback;
    }

    private String blankToNull(String value) {
        return (value != null && !value.isBlank()) ? value.trim() : null;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
