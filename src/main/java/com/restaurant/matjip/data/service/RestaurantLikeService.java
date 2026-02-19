package com.restaurant.matjip.data.service;

import com.restaurant.matjip.data.domain.Restaurant;
import com.restaurant.matjip.data.domain.RestaurantLike;
import com.restaurant.matjip.data.repository.RestaurantLikeRepository;
import com.restaurant.matjip.data.repository.RestaurantRepository;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantLikeService {

    private final RestaurantLikeRepository likeRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    /* ================= 좋아요 ================= */

    public void likeByEmail(String email, Long restaurantId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (likeRepository.existsByUser_IdAndRestaurant_Id(user.getId(), restaurantId)) {
            return;
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        likeRepository.save(new RestaurantLike(user, restaurant));
    }

    /* ================= 좋아요 취소 ================= */

    public void unlikeByEmail(String email, Long restaurantId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        likeRepository.deleteByUser_IdAndRestaurant_Id(user.getId(), restaurantId);
    }

    /* ================= 좋아요 개수 ================= */

    @Transactional(readOnly = true)
    public long count(Long restaurantId) {
        return likeRepository.countByRestaurant_Id(restaurantId);
    }
}
