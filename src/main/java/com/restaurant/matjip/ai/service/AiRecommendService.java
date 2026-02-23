package com.restaurant.matjip.ai.service;

import com.restaurant.matjip.ai.client.AiClient;
import com.restaurant.matjip.ai.dto.AiRecommendRequest;
import com.restaurant.matjip.ai.dto.AiRecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiRecommendService {

    private final AiClient aiClient;
    private final CategoryPreferenceService categoryPreferenceService;

    public AiRecommendResponse recommend(
            String question,
            Long userId,
            Double lat,
            Double lng
    ) {
        // 🔥 좋아요 기반 선호 카테고리 Top 3
        List<String> preferredCategories =
                categoryPreferenceService.getTopCategories(userId);

        AiRecommendRequest request = AiRecommendRequest.builder()
                .question(question)
                .userId(userId)
                .preferredCategories(preferredCategories) // 없으면 빈 리스트
                .lat(lat)
                .lng(lng)
                .build();

        return aiClient.requestRecommend(request);
    }
}
