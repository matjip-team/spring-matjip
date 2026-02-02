package com.restaurant.matjip.mypage.repository;

import com.restaurant.matjip.community.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 로그인한 사용자(userId) 기준 최신순 조회
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);
}
