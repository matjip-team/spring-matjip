package com.restaurant.matjip.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class RestaurantCreateRequest {

    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String phone;
    private String description;

    /** Python 수집기에서 문자열로 전달 */
    private List<String> categoryNames;
}
