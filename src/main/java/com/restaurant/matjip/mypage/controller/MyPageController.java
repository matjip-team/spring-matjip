package com.restaurant.matjip.mypage.controller;

import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.mypage.dto.request.UserInfoRequest;
import com.restaurant.matjip.mypage.dto.response.ReviewPageResponse;
import com.restaurant.matjip.mypage.dto.response.ReviewResponse;
import com.restaurant.matjip.mypage.dto.response.UserInfoResponse;
import com.restaurant.matjip.mypage.service.MyPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;
    private final MessageSource messageSource;

//    @GetMapping("/reviews")
//    public List<ReviewResponse> getReviews(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        return myPageService.getUserReviews(1L);
//    }

    @GetMapping("/reviews")
    public ReviewPageResponse getReviews(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "20") int limit) {

        ReviewPageResponse reviewPageResponse = myPageService.getUserReviews(1L, cursor, limit);
        log.debug(reviewPageResponse.getReview().size() + "");
        return reviewPageResponse;
    }

    @GetMapping("/recommendations")
    public ReviewPageResponse getRecommendations(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "20") int limit) {
        return myPageService.getUserReviews(1L, cursor, limit);
    }

    @GetMapping("/profile")
    public ApiResponse<UserInfoResponse> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.success(myPageService.getUserInfo(1L));
    }

    @PutMapping("/profile")
    public ApiResponse<Void> updateProfile(
            @ModelAttribute @Valid UserInfoRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails, Locale locale) {

        myPageService.updateProfile(1L,request);
        String msg = messageSource.getMessage("success.user.updated",null, locale);
        return ApiResponse.success(msg);
    }

}
