package com.restaurant.matjip.data.repository;

import com.restaurant.matjip.data.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("""
        select distinct r from Restaurant r
        left join r.categories c
        where (:categories is null or c.name in :categories)
    """)
    List<Restaurant> searchByCategories(List<String> categories);
}
