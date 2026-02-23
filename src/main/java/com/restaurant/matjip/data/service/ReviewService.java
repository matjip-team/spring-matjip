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

    /* ================= 리뷰 작성 ================= */
    public void createByEmail(String email, Long restaurantId, ReviewCreateRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        validateRating(request.getRating());

        Review review = new Review(
                user,
                restaurant,
                request.getRating(),
                request.getContent()
        );

        reviewRepository.save(review);
    }

    /* ================= 리뷰 수정 ================= */
    public void updateByEmail(String email, Long reviewId, ReviewCreateRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Review review = reviewRepository.findByIdAndUser_Id(reviewId, user.getId())
                .orElseThrow(() -> new IllegalStateException("수정 권한이 없습니다."));

        validateRating(request.getRating());

        review.update(request.getRating(), request.getContent());
    }

    /* ================= 리뷰 삭제 ================= */
    public void deleteByEmail(String email, Long reviewId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Review review = reviewRepository.findByIdAndUser_Id(reviewId, user.getId())
                .orElseThrow(() -> new IllegalStateException("삭제 권한이 없습니다."));

        reviewRepository.delete(review);
    }

    /* ================= 리뷰 목록 ================= */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviews(Long restaurantId, String currentUserEmail) {

        return reviewRepository.findByRestaurant_Id(restaurantId)
                .stream()
                .map(review -> ReviewResponse.from(review, currentUserEmail))
                .toList();
    }

    /* ================= 평균 평점 ================= */
    @Transactional(readOnly = true)
    public double getAverageRating(Long restaurantId) {

        Double avg = reviewRepository.findAverageRating(restaurantId);

        return avg == null ? 0.0 : Math.round(avg * 10) / 10.0;
    }

    /* ================= 평점 검증 ================= */
    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("평점은 1~5 사이여야 합니다.");
        }
    }
}
