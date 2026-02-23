package com.restaurant.matjip.ai.controller;

import com.restaurant.matjip.ai.dto.AiRecommendResponse;
import com.restaurant.matjip.ai.service.AiRecommendService;
import com.restaurant.matjip.global.common.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AiRecommendController {

    private final AiRecommendService aiRecommendService;

    public AiRecommendController(AiRecommendService aiRecommendService) {
        this.aiRecommendService = aiRecommendService;
    }

    @PostMapping("/recommend")
    public AiRecommendResponse recommend(
            @RequestBody String question,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long userId = (user != null) ? user.getId() : null;

        return aiRecommendService.recommend(
                question,
                userId,
                null,   // lat (추후 확장)
                null    // lng
        );
    }
}
