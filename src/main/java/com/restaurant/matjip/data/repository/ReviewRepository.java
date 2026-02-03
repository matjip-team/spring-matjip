package com.restaurant.matjip.data.repository;

import com.restaurant.matjip.data.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRestaurant_Id(Long restaurantId);

    boolean existsByUser_IdAndRestaurant_Id(Long userId, Long restaurantId);

    @Query("select avg(r.rating) from Review r where r.restaurant.id = :restaurantId")
    Double findAverageRating(Long restaurantId);
}
