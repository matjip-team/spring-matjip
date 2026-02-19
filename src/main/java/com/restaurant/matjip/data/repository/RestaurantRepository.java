package com.restaurant.matjip.data.repository;

import com.restaurant.matjip.data.domain.Restaurant;
import com.restaurant.matjip.data.domain.RestaurantApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("""
        select distinct r from Restaurant r
        left join r.categories c
        where r.approvalStatus = com.restaurant.matjip.data.domain.RestaurantApprovalStatus.APPROVED
          and (:categories is null or c.name in :categories)
    """)
    List<Restaurant> searchByCategories(List<String> categories);

    List<Restaurant> findAllByApprovalStatusOrderByCreatedAtDesc(RestaurantApprovalStatus approvalStatus);

    List<Restaurant> findAllByRegisteredByIdOrderByCreatedAtDesc(Long registeredById);

    Optional<Restaurant> findByIdAndRegisteredById(Long id, Long registeredById);

    boolean existsByExternalId(String externalId);
}
