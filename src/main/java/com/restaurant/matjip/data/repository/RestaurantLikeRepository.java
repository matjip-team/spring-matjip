package com.restaurant.matjip.data.repository;

import com.restaurant.matjip.data.domain.RestaurantLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantLikeRepository extends JpaRepository<RestaurantLike, Long> {

    boolean existsByUser_IdAndRestaurant_Id(Long userId, Long restaurantId);

    void deleteByUser_IdAndRestaurant_Id(Long userId, Long restaurantId);

    long countByRestaurant_Id(Long restaurantId);
}
