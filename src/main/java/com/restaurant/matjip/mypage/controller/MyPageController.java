package com.restaurant.matjip.mypage.controller;

import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.mypage.dto.response.ReviewResponse;
import com.restaurant.matjip.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/reviews")
    public List<ReviewResponse> getReviews(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return myPageService.getUserReviews(customUserDetails.getId());
    }
}
