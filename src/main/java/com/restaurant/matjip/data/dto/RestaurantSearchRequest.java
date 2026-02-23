package com.restaurant.matjip.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class RestaurantSearchRequest {

    // 카테고리 리스트를 동적으로 분류
    private List<String> categories;

    // 검색 키워드
    private String keyword;

    public void setCategories(String categories) {
        if (categories == null || categories.isBlank()) {
            this.categories = null;
            return;
        }
        this.categories = Arrays.stream(categories.split(","))
                .map(String::trim)
                .toList();
    }
    //  keyword가 빈 문자열이면 null 처리
    public void setKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            this.keyword = null;
        } else {
            this.keyword = keyword.trim();
        }
    }
}
