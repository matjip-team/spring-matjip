package com.restaurant.matjip.data.dto;

import com.restaurant.matjip.data.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;
    private String name;

    public static CategoryDTO fromEntity(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName()
        );
    }
}
