package com.restaurant.matjip.users.repository;

import com.restaurant.matjip.users.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
