package com.restaurant.matjip.mypage.controller;

import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.mypage.dto.request.UserInfoRequest;
import com.restaurant.matjip.mypage.dto.response.ReviewResponse;
import com.restaurant.matjip.mypage.dto.response.UserInfoResponse;
import com.restaurant.matjip.mypage.service.MyPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/reviews")
    public List<ReviewResponse> getReviews(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return myPageService.getUserReviews(1L);
    }

    @GetMapping("/recommendations")
    public List<ReviewResponse> getRecommendations(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return myPageService.getUserReviews(1L);
    }

    @GetMapping("/userInfo")
    public UserInfoResponse getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return myPageService.getUserInfo(1L);
    }

    @PutMapping("/profile")
    public ApiResponse<Void> updateProfile(
            @ModelAttribute  UserInfoRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        myPageService.updateProfile(1L,request);

        return ApiResponse.success(null);
    }

}
