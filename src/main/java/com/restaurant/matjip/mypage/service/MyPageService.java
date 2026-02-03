package com.restaurant.matjip.mypage.service;

import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import com.restaurant.matjip.mypage.dto.request.UserInfoRequest;
import com.restaurant.matjip.mypage.dto.response.ReviewResponse;
import com.restaurant.matjip.mypage.dto.response.UserInfoResponse;
import com.restaurant.matjip.mypage.repository.ReviewRepository2;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.domain.UserProfile;
import com.restaurant.matjip.users.repository.UserProfileRepository;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final ReviewRepository2 reviewRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public List<ReviewResponse> getUserReviews(Long userId) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        UserProfile userProfile = userProfileRepository.findById(user.getId()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return UserInfoResponse.from(user, userProfile);

    }

    public void updateProfile(long id, UserInfoRequest request) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        userProfile.setNickname(request.getNickname());
        userProfile.setBio(request.getBio());
        userProfile.setProfileImageUrl(request.getProfileImageUrl());

        userProfileRepository.save(userProfile);
    }
}
