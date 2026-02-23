package com.restaurant.matjip.mypage.repository;

import com.restaurant.matjip.data.domain.Review;
import com.restaurant.matjip.mypage.dto.response.ReviewResponse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository2 extends JpaRepository<Review, Long> {
    // 로그인한 사용자(userId) 기준 최신순 조회
    @EntityGraph(attributePaths = {"user", "restaurant"})
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
    SELECT new com.restaurant.matjip.mypage.dto.response.ReviewResponse(
         r.id,
         r.rating,
         r.content,
         r.createdAt,
         r.updatedAt,
         res.id,
         res.name,
         res.address,
         res.imageUrl,
         COALESCE(AVG(rv.rating), 0),
         COUNT(rv.id),
         c.id,
         c.name
     )
     FROM Review r
     JOIN r.restaurant res
     LEFT JOIN res.categories c
     LEFT JOIN Review rv ON rv.restaurant.id = res.id
     WHERE (:cursorId IS NULL OR r.id > :cursorId)
     GROUP BY r.id, r.createdAt, r.updatedAt,
              res.id, res.name, c.id, c.name
     ORDER BY r.id ASC
    """)
    List<ReviewResponse> findNextReview(@Param("userId") Long userId, @Param("cursorId") Long cursorId);

}
