package com.restaurant.matjip.ai.repository;

import com.restaurant.matjip.data.domain.RestaurantLike;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AiPreferenceRepository extends Repository<RestaurantLike, Long> {

    @Query("""
        SELECT c.name, COUNT(rl)
        FROM RestaurantLike rl
        JOIN rl.restaurant r
        JOIN r.categories c
        WHERE rl.user.id = :userId
        GROUP BY c.name
        ORDER BY COUNT(rl) DESC
    """)
    List<Object[]> findTopCategoriesByUser(@Param("userId") Long userId);
}
