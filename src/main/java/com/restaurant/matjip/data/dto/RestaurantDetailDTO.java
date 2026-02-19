package com.restaurant.matjip.data.dto;

import com.restaurant.matjip.data.domain.Restaurant;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class RestaurantDetailDTO {

    private Long id;
    private String name;
    private String address;
    private String description;
    private String imageUrl;

    private Set<String> categories;

    // 리뷰
    private double averageRating;
    private int reviewCount;
    private List<ReviewResponse> reviews;

    // 좋아요
    private long likeCount;
    private boolean liked;

    public static RestaurantDetailDTO from(
            Restaurant restaurant,
            double averageRating,
            int reviewCount,
            List<ReviewResponse> reviews,
            long likeCount,
            boolean liked
    ) {
        return RestaurantDetailDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .description(restaurant.getDescription())
                .imageUrl(restaurant.getImageUrl())
                .categories(
                        restaurant.getCategories()
                                .stream()
                                .map(c -> c.getName())
                                .collect(Collectors.toSet())
                )
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .reviews(reviews)
                .likeCount(likeCount)
                .liked(liked)
                .build();
    }
}
