package com.restaurant.matjip.mypage.controller;

import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.mypage.dto.request.UserInfoRequest;
import com.restaurant.matjip.mypage.dto.response.ReviewResponse;
import com.restaurant.matjip.mypage.dto.response.UserInfoResponse;
import com.restaurant.matjip.mypage.service.MyPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;
    private final MessageSource messageSource;

    @GetMapping("/reviews")
    public List<ReviewResponse> getReviews(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return myPageService.getUserReviews(1L);
    }

    @GetMapping("/recommendations")
    public List<ReviewResponse> getRecommendations(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return myPageService.getUserReviews(1L);
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
