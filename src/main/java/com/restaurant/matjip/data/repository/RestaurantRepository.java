package com.restaurant.matjip.data.repository;

import com.restaurant.matjip.data.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    /* 기존: 카테고리 검색 */
    @Query("""
        select distinct r from Restaurant r
        left join r.categories c
        where (:categories is null or c.name in :categories)
    """)
    List<Restaurant> searchByCategories(List<String> categories);

    /* ⭐ 추가: Python 수집 중복 방지 */
    boolean existsByExternalId(String externalId);
}
