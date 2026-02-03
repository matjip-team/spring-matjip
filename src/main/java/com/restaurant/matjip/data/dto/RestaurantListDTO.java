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
    private Set<String> categories;

    public static RestaurantListDTO from(Restaurant r) {
        return new RestaurantListDTO(
                r.getId(),
                r.getName(),
                r.getAddress(),
                r.getCategories().stream()
                        .map(Category::getName)
                        .collect(Collectors.toSet())
        );
    }
}
