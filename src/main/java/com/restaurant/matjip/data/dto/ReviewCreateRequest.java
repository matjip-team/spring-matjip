package com.restaurant.matjip.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateRequest {
    private int rating;     // 1~5
    private String content;
}
