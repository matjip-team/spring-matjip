package com.restaurant.matjip.data.controller;

import com.restaurant.matjip.data.dto.RestaurantListDTO;
import com.restaurant.matjip.data.dto.RestaurantMapDTO;
import com.restaurant.matjip.data.dto.RestaurantSearchRequest;
import com.restaurant.matjip.data.service.RestaurantService;
import com.restaurant.matjip.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    /* 맛집 리스트 */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<RestaurantListDTO>> getRestaurants(
            RestaurantSearchRequest request
    ) {
        List<RestaurantListDTO> response = restaurantService.search(request);
        return ApiResponse.success(response);
    }

    /* 지도용 맛집 */
    @GetMapping("/map")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<RestaurantMapDTO>> getRestaurantsForMap(
            RestaurantSearchRequest request
    ) {
        List<RestaurantMapDTO> response = restaurantService.searchForMap(request);
        return ApiResponse.success(response);
    }
}
