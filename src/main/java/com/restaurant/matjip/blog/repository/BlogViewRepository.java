package com.restaurant.matjip.blog.repository;

import com.restaurant.matjip.blog.domain.BlogView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogViewRepository
        extends JpaRepository<BlogView, Long> {

    /* ================== 조회 여부 확인 ================== */

    boolean existsByBlogIdAndUserId(Long blogId, Long userId);

    void deleteByBlogId(Long blogId);
}

