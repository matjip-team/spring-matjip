package com.restaurant.matjip.blog.repository;

import com.restaurant.matjip.blog.domain.BlogRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRecommendationRepository
        extends JpaRepository<BlogRecommendation, Long> {

    /* 추천 여부 */
    boolean existsByBlogIdAndUserId(Long blogId, Long userId);

    /* 추천 취소 */
    void deleteByBlogIdAndUserId(Long blogId, Long userId);

    /* 추천 수 */
    int countByBlogId(Long blogId);
}


