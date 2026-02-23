package com.restaurant.matjip.ai.client;

import com.restaurant.matjip.ai.dto.AiRecommendRequest;
import com.restaurant.matjip.ai.dto.AiRecommendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AiClient {

    private final WebClient webClient;

    public AiClient(@Value("${ai.base-url:http://localhost:8000}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public AiRecommendResponse requestRecommend(AiRecommendRequest request) {
        return webClient.post()
                .uri("/recommend/")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AiRecommendResponse.class)
                .block();
    }
}
