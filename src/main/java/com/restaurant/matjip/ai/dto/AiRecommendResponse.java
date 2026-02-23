package com.restaurant.matjip.ai.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class AiRecommendResponse {

    private Map<String, Object> analysis;
    private List<Map<String, Object>> recommended_places;
    private String ai_comment;
}
