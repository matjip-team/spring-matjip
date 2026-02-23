package com.restaurant.matjip.admin.blog.service;

import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.domain.BlogRecommendation;
import com.restaurant.matjip.admin.blog.repository.AdminBlogRecommendationRepository;
import com.restaurant.matjip.admin.blog.repository.AdminBlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminBlogRecommendationService {

    private final AdminBlogRepository blogRepository;
    private final AdminBlogRecommendationRepository blogRecommendationRepository;

    @Transactional
    public void toggleRecommend(Long blogId, Long userId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow();

        if (blogRecommendationRepository.existsByBlogIdAndUserId(blogId, userId)) {
            blogRecommendationRepository.deleteByBlogIdAndUserId(blogId, userId);
            blog.decreaseRecommendCount();
            return;
        }

        BlogRecommendation rec = BlogRecommendation.builder()
                .blog(blog)
                .userId(userId)
                .build();
        blogRecommendationRepository.save(rec);
        blog.increaseRecommendCount();
    }
}
