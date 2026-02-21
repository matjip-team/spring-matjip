package com.restaurant.matjip.admin.user.repository;

import com.restaurant.matjip.users.constant.UserStatus;
import com.restaurant.matjip.users.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminUserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<User> findAllByStatusOrderByCreatedAtDesc(UserStatus status, Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
            "(:status IS NULL OR u.status = :status) AND " +
            "((:searchType = 'EMAIL' AND LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:searchType = 'NAME' AND LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:searchType = 'NICKNAME' AND LOWER(u.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))))")
    Page<User> searchUsers(
            @Param("keyword") String keyword,
            @Param("searchType") String searchType,
            @Param("status") UserStatus status,
            Pageable pageable);
}
