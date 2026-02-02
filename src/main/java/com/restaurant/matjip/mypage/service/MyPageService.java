package com.restaurant.matjip.mypage.service;

import com.restaurant.matjip.mypage.dto.response.ReviewResponse;
import com.restaurant.matjip.mypage.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final ReviewRepository reviewRepository;

    public List<ReviewResponse> getUserReviews(Long userId) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
    }
}
