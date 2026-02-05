package com.restaurant.matjip.mypage.repository;

import com.restaurant.matjip.data.domain.RestaurantLike;
import com.restaurant.matjip.mypage.dto.response.LikeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantLikeRepository2 extends JpaRepository<RestaurantLike, Long> {

    // 커서 기반 조회: ID > lastId 순서로 limit
    @Query("""
        SELECT new com.restaurant.matjip.mypage.dto.response.LikeResponse(
             r.id,
             r.createdAt,
             r.updatedAt,
             res.id,
             res.name,
             COALESCE(AVG(rv.rating), 0),
             COUNT(rv.id)
         )
         FROM RestaurantLike r
         JOIN r.restaurant res
         LEFT JOIN Review rv ON rv.restaurant.id = res.id
         WHERE (:cursorId IS NULL OR r.id > :cursorId)
         GROUP BY r.id, r.createdAt, r.updatedAt, res.id, res.name
         ORDER BY r.id ASC
    """)
    List<LikeResponse> findNextLike(@Param("cursorId") Long cursorId, Pageable pageable);
}
