package com.restaurant.matjip.data.repository;

import com.restaurant.matjip.data.domain.Restaurant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.restaurant.matjip.data.domain.RestaurantApprovalStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {


    /* 카테고리 + 이름/주소 검색 */
    @Query("""
    select distinct r from Restaurant r
    left join r.categories c
    where (:categories is null or c.name in :categories)
      and (:keyword is null or r.name like %:keyword% or r.address like %:keyword%)
""")
    Page<Restaurant> search(
            List<String> categories,
            String keyword,
            Pageable pageable
    );

    /* Python 수집 중복 방지 */
    @Query("""
        select distinct r from Restaurant r
        left join r.categories c
        where r.approvalStatus = com.restaurant.matjip.data.domain.RestaurantApprovalStatus.APPROVED
          and (:categories is null or c.name in :categories)
    """)
    List<Restaurant> searchByCategories(List<String> categories);

    List<Restaurant> findAllByApprovalStatusOrderByCreatedAtDesc(RestaurantApprovalStatus approvalStatus);
    List<Restaurant> findAllByApprovalStatusAndSourceOrderByCreatedAtDesc(
            RestaurantApprovalStatus approvalStatus,
            String source
    );

    List<Restaurant> findAllByRegisteredByIdOrderByCreatedAtDesc(Long registeredById);

    Optional<Restaurant> findByIdAndRegisteredById(Long id, Long registeredById);


    boolean existsByExternalId(String externalId);
}
