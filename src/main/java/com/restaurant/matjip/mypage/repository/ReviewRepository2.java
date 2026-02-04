package com.restaurant.matjip.mypage.repository;

import com.restaurant.matjip.data.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository2 extends JpaRepository<Review, Long> {
    // 로그인한 사용자(userId) 기준 최신순 조회
    @EntityGraph(attributePaths = {"user", "restaurant"})
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 커서 기반 조회: ID > lastId 순서로 limit
    @Query("""
    SELECT r FROM Review r
    JOIN FETCH r.user
    JOIN FETCH r.restaurant
    WHERE (:cursorId IS NULL OR r.id > :cursorId)
    ORDER BY r.id ASC
""")
    List<Review> findNextReview(@Param("cursorId") Long cursorId, Pageable pageable);

}
