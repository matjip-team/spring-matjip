package com.restaurant.matjip.admin.blog.repository;

import com.restaurant.matjip.blog.domain.BlogView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminBlogViewRepository extends JpaRepository<BlogView, Long> {

    boolean existsByBlogIdAndUserId(Long blogId, Long userId);

    void deleteByBlogId(Long blogId);
}
