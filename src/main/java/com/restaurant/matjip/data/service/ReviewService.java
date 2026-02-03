package com.restaurant.matjip.data.service;

import com.restaurant.matjip.data.domain.Restaurant;
import com.restaurant.matjip.data.domain.Review;
import com.restaurant.matjip.data.dto.ReviewCreateRequest;
import com.restaurant.matjip.data.dto.ReviewResponse;
import com.restaurant.matjip.data.repository.RestaurantRepository;
import com.restaurant.matjip.data.repository.ReviewRepository;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    /** 리뷰 작성 */
    public void create(Long userId, Long restaurantId, ReviewCreateRequest request) {
        if (reviewRepository.existsByUser_IdAndRestaurant_Id(userId, restaurantId)) {
            throw new IllegalStateException("이미 리뷰를 작성했습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        Review review = new Review(
                user,
                restaurant,
                request.getRating(),
                request.getContent()
        );

        reviewRepository.save(review);
    }

    /** 리뷰 목록 */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviews(Long restaurantId) {
        return reviewRepository.findByRestaurant_Id(restaurantId)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    /** 평균 평점 */
    @Transactional(readOnly = true)
    public double getAverageRating(Long restaurantId) {
        Double avg = reviewRepository.findAverageRating(restaurantId);
        return avg == null ? 0.0 : Math.round(avg * 10) / 10.0;
    }
}
