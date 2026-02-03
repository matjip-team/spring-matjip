package com.restaurant.matjip.mypage.repository;

import com.restaurant.matjip.data.domain.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository2 extends JpaRepository<Review, Long> {
    // 로그인한 사용자(userId) 기준 최신순 조회
    @EntityGraph(attributePaths = {"user", "restaurant"})
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);
}
