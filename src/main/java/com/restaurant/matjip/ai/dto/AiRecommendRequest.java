package com.restaurant.matjip.ai.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AiRecommendRequest {

    private String question;
    private Long userId;
    private List<String> preferredCategories;
    private Double lat;
    private Double lng;
}
