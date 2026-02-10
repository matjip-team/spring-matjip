package com.restaurant.matjip.ai.client;

import com.restaurant.matjip.ai.dto.AiRecommendRequest;
import com.restaurant.matjip.ai.dto.AiRecommendResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AiClient {

    private final WebClient webClient;

    public AiClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8000")
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
