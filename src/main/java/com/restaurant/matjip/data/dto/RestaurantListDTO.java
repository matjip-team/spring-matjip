package com.restaurant.matjip.data.dto;

import com.restaurant.matjip.data.domain.Category;
import com.restaurant.matjip.data.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class RestaurantListDTO {

    private Long id;
    private String name;
    private String address;

    /** DB에 실제 저장된 이미지 URL (없으면 null) */
    private String imageUrl;

    /** React 편의용: 대표 카테고리 1개 */
    private String category;

    /** (옵션) 전체 카테고리 목록 */
    private Set<String> categories;

    /** ✅ 추가 */
    private long likeCount;
    private boolean liked;

    private static final String DEFAULT_CATEGORY = "일중/세계음식";

    public static RestaurantListDTO from(
            Restaurant r,
            long likeCount,
            boolean liked
    ) {

        Set<String> categorySet = r.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet());

        String mainCategory = categorySet.stream()
                .findFirst()
                .orElse(DEFAULT_CATEGORY);

        String imageUrl =
                (r.getImageUrl() != null && !r.getImageUrl().isBlank())
                        ? r.getImageUrl()
                        : null;

        return new RestaurantListDTO(
                r.getId(),
                r.getName(),
                r.getAddress(),
                imageUrl,
                mainCategory,
                categorySet,
                likeCount,
                liked
        );
    }
}
