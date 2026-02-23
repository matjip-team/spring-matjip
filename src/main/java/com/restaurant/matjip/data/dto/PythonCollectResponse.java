package com.restaurant.matjip.data.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class PythonCollectResponse {
    private int count;
    private List<PythonRestaurantDto> data;
}
