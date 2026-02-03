package com.restaurant.matjip.data.dto;

import com.restaurant.matjip.data.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RestaurantMapDTO {

    private Long id;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public static RestaurantMapDTO from(Restaurant r) {
        return new RestaurantMapDTO(
                r.getId(),
                r.getName(),
                r.getLatitude(),
                r.getLongitude()
        );
    }
}
