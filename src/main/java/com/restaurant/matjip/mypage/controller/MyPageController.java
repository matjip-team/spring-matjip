package com.restaurant.matjip.mypage.controller;

import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.mypage.dto.request.UserInfoRequest;
import com.restaurant.matjip.mypage.dto.response.LikePageResponse;
import com.restaurant.matjip.mypage.dto.response.ReviewPageResponse;
import com.restaurant.matjip.mypage.dto.response.UserInfoResponse;
import com.restaurant.matjip.mypage.service.MyPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;
    private final MessageSource messageSource;

    @GetMapping("/reviews")
    public ApiResponse<ReviewPageResponse> getReviews(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "20") int limit) {
        return ApiResponse.success(myPageService.getUserReviews(customUserDetails.getId(), cursor, limit));
    }

    @GetMapping("/likes")
    public ApiResponse<LikePageResponse> getLikes(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "20") int limit) {
        return ApiResponse.success(myPageService.getLikes(customUserDetails.getId(), cursor, limit));
    }

    @GetMapping("/profile")
    public ApiResponse<UserInfoResponse> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.success(myPageService.getUserInfo(customUserDetails.getId()));
    }

    @PutMapping("/profile")
    public ApiResponse<Void> updateProfile(
            @ModelAttribute @Valid UserInfoRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails, Locale locale) {

        myPageService.updateProfile(customUserDetails.getId(), request);
        String msg = messageSource.getMessage("success.user.updated",null, locale);
        return ApiResponse.success(msg);
    }

    @DeleteMapping("/likes/{likeId}")
    public ApiResponse<Void> deleteLikes(
            @PathVariable Long likeId, @AuthenticationPrincipal CustomUserDetails customUserDetails, Locale locale) {

        myPageService.deleteLikes(likeId, customUserDetails.getId());

        return ApiResponse.success(null);
    }

}
