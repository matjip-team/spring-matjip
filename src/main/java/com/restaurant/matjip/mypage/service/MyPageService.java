package com.restaurant.matjip.mypage.service;

import com.restaurant.matjip.data.domain.RestaurantLike;
import com.restaurant.matjip.data.domain.Review;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import com.restaurant.matjip.mypage.dto.request.UserInfoRequest;
import com.restaurant.matjip.mypage.dto.response.LikePageResponse;
import com.restaurant.matjip.mypage.dto.response.LikeResponse;
import com.restaurant.matjip.mypage.dto.response.ReviewPageResponse;
import com.restaurant.matjip.mypage.dto.response.UserInfoResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {
    private final ReviewRepository2 reviewRepository;
    private final RestaurantLikeRepository2 restaurantLikeRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${uploadPath}")
    private String uploadDir;

    @Value("${productImageLocation}")
    private String productImageLocation ; // 기본 값 :

    public LikePageResponse getLikes(long l, Long cursorId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<LikeResponse> like = restaurantLikeRepository.findNextLike(cursorId, pageable);

        Long nextCursor = like.isEmpty() ? null : like.getLast().getId();

        return LikePageResponse.from(like, nextCursor);
    }

    public ReviewPageResponse getUserReviews(Long userId, Long cursorId, int limit) {

        Pageable pageable = PageRequest.of(0, limit);
        List<Review> review = reviewRepository.findNextReview(cursorId, pageable);

        Long nextCursor = review.isEmpty() ? null : review.getLast().getId();

        return ReviewPageResponse.from(review, nextCursor);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        UserProfile userProfile = userProfileRepository.findById(user.getId()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return UserInfoResponse.from(user, userProfile);

    }

    public void updateProfile(long id, UserInfoRequest request) {
        // 패스워드 변경
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            User user = userRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            // 회원 패스워드 변경
            userRepository.save(user);
        }

        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 프로필 이미지 처리
        MultipartFile file = request.getProfileImage();
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = userProfile.getUserId() + "_" + System.currentTimeMillis() + extension;

            File dest = Paths.get(productImageLocation, fileName).toFile();
            File parentDir = dest.getParentFile();
            if (!parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                if (!created) {
                    throw new RuntimeException();
                }
            }
            boolean created = dest.getParentFile().mkdirs();

            try {
                file.transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException();
            }
            // 기존 파일 삭제
            if (userProfile.getProfileImageUrl() != null) {
                File oldFile = Paths.get(productImageLocation, userProfile.getProfileImageUrl()).toFile();
                if (oldFile.exists()) {                    ;
                    if (!oldFile.delete()) {
                        //파일이없을 수도 있음
                        //throw new BusinessException(ErrorCode.INTERNAL_ERROR);
                        log.debug("삭제 실패");
                    }
                }
            }
            userProfile.setProfileImageUrl(fileName);
        }

        // 회원프로파일 변경
        userProfile.setNickname(request.getNickname());
        userProfile.setBio(request.getBio());

        userProfileRepository.save(userProfile);
    }
}
