package com.restaurant.matjip.admin.blog.repository;

import com.restaurant.matjip.blog.domain.BlogRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminBlogRecommendationRepository extends JpaRepository<BlogRecommendation, Long> {

    boolean existsByBlogIdAndUserId(Long blogId, Long userId);

    void deleteByBlogIdAndUserId(Long blogId, Long userId);

    int countByBlogId(Long blogId);
}
