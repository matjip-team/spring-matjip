package com.restaurant.matjip.mypage.service;

import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import com.restaurant.matjip.mypage.dto.request.UserInfoRequest;
import com.restaurant.matjip.mypage.dto.response.*;
import com.restaurant.matjip.mypage.repository.RestaurantLikeRepository2;
import com.restaurant.matjip.mypage.repository.ReviewRepository2;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.domain.UserProfile;
import com.restaurant.matjip.users.repository.UserProfileRepository;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {
    private final ReviewRepository2 reviewRepository;
    private final RestaurantLikeRepository2 restaurantLikeRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${uploadPath}")
    private String uploadDir;

    @Value("${productImageLocation}")
    private String productImageLocation;

    @Transactional(readOnly = true)
    public LikePageResponse getLikes(long userId, Long cursorId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<LikeResponse> like = restaurantLikeRepository.findNextLike(userId, cursorId);

        Map<Long, LikeResponse> grouped = new LinkedHashMap<>();

        for (LikeResponse row : like) {
            Long restaurantId = row.getRestaurantId();
            LikeResponse existing = grouped.computeIfAbsent(restaurantId, id -> {
                row.setCategories(new ArrayList<>());
                return row;
            });

            if (row.getCategoryId() != null) {
                existing.getCategories().add(
                        new CategroyResponse(row.getCategoryId(), row.getCategoryName())
                );
            }
        }

        List<LikeResponse> page = grouped.values().stream()
                .limit(limit)
                .toList();

        Long nextCursor = page.isEmpty() ? null : page.getLast().getId();

        return LikePageResponse.from(page, nextCursor);
    }

    public void deleteLikes(long id, Long userId) {
        restaurantLikeRepository.deleteByIdAndUserId(id, userId);
    }

    @Transactional(readOnly = true)
    public ReviewPageResponse getUserReviews(Long userId, Long cursorId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<ReviewResponse> review = reviewRepository.findNextReview(userId, cursorId);

        Map<Long, ReviewResponse> grouped = new LinkedHashMap<>();

        for (ReviewResponse row : review) {
            Long restaurantId = row.getRestaurantId();
            ReviewResponse existing = grouped.computeIfAbsent(restaurantId, id -> {
                row.setCategories(new ArrayList<>());
                return row;
            });

            if (row.getCategoryId() != null) {
                existing.getCategories().add(
                        new CategroyResponse(row.getCategoryId(), row.getCategoryName())
                );
            }
        }

        List<ReviewResponse> page = grouped.values().stream()
                .limit(limit)
                .toList();

        Long nextCursor = page.isEmpty() ? null : page.getLast().getId();

        return ReviewPageResponse.from(review, nextCursor);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        UserProfile userProfile = userProfileRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return UserInfoResponse.from(user, userProfile);
    }

    public void updateProfile(long id, UserInfoRequest request) {
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
        }

        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String profileImageUrl = request.getProfileImageUrl();
        if (profileImageUrl != null && !profileImageUrl.isBlank()) {
            userProfile.setProfileImageUrl(profileImageUrl.trim());
        }
//S3로 변경함
//        MultipartFile file = request.getProfileImage();
//        if (file != null && !file.isEmpty()) {
//            String originalFilename = file.getOriginalFilename();
//            String extension = "";
//            if (originalFilename != null && originalFilename.contains(".")) {
//                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            }
//            String fileName = userProfile.getUserId() + "_" + System.currentTimeMillis() + extension;
//
//            File dest = Paths.get(productImageLocation, fileName).toFile();
//            File parentDir = dest.getParentFile();
//            if (!parentDir.exists() && !parentDir.mkdirs()) {
//                throw new RuntimeException();
//            }
//
//            try {
//                file.transferTo(dest);
//            } catch (IOException e) {
//                throw new RuntimeException();
//            }
//
//            if (userProfile.getProfileImageUrl() != null) {
//                File oldFile = Paths.get(productImageLocation, userProfile.getProfileImageUrl()).toFile();
//                if (oldFile.exists() && !oldFile.delete()) {
//                    log.debug("delete failed");
//                }
//            }
//            userProfile.setProfileImageUrl(fileName);
//        }

        userProfile.setNickname(request.getNickname());
        userProfile.setBio(request.getBio());

        userProfileRepository.save(userProfile);
    }

    @Transactional
    public void withdraw(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if ("DELETED".equals(user.getStatus().name())) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
        user.withdraw();
    }
}