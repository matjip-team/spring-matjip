package com.restaurant.matjip.blog.service;

import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.domain.BlogRecommendation;
import com.restaurant.matjip.blog.repository.BlogRepository;
import com.restaurant.matjip.blog.repository.BlogRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogRecommendationService {

    private final BlogRepository blogRepository;
    private final BlogRecommendationRepository blogRecommendationRepository;

    @Transactional
    public void toggleRecommend(Long blogId, Long userId) {

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow();

        // 이미 추천했으면 취소
        if (blogRecommendationRepository.existsByBlogIdAndUserId(blogId, userId)) {

            blogRecommendationRepository.deleteByBlogIdAndUserId(blogId, userId);
            blog.decreaseRecommendCount();
            return;
        }

        // 추천
        BlogRecommendation rec = BlogRecommendation.builder()
                .blog(blog)
                .userId(userId)
                .build();

        blogRecommendationRepository.save(rec);
        blog.increaseRecommendCount();
    }
}


