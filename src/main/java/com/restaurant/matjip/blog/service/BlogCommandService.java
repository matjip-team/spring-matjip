package com.restaurant.matjip.blog.service;

import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.dto.request.BlogCreateRequest;
import com.restaurant.matjip.blog.dto.request.BlogUpdateRequest;
import com.restaurant.matjip.blog.repository.BlogRepository;
import com.restaurant.matjip.blog.repository.BlogViewRepository;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogCommandService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final BlogViewRepository blogViewRepository;

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

    @Transactional
    public void update(Long blogId, Long userId, BlogUpdateRequest req) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow();

        if (!blog.getUser().getId().equals(userId)) {
            throw new RuntimeException("no permission");
        }

        blog.setTitle(req.getTitle());
        blog.setContent(resolvePrimaryContent(req.getContent(), req.getContentHtml(), blog.getContent()));
        blog.setContentHtml(resolveHtmlContent(req.getContentHtml(), req.getContent(), blog.getContentHtml()));
        blog.setContentDelta(blankToNull(req.getContentDelta()));
        blog.setImageUrl(blankToNull(req.getImageUrl()));
        blog.setBlogType(req.getBlogType());
    }

    @Transactional
    public void delete(Long blogId, Long userId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow();

        if (!blog.getUser().getId().equals(userId)) {
            throw new RuntimeException("no permission");
        }

        blogViewRepository.deleteByBlogId(blogId);
        blogRepository.delete(blog);
    }

    private String resolvePrimaryContent(String content, String contentHtml, String fallback) {
        if (hasText(content)) {
            return content;
        }
        if (hasText(contentHtml)) {
            return contentHtml;
        }
        return fallback;
    }

    private String resolveHtmlContent(String contentHtml, String content, String fallback) {
        if (hasText(contentHtml)) {
            return contentHtml;
        }
        if (hasText(content)) {
            return content;
        }
        return fallback;
    }

    private String blankToNull(String value) {
        if (!hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
