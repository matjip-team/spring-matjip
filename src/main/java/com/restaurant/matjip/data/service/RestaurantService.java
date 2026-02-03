package com.restaurant.matjip.data.service;

import com.restaurant.matjip.data.domain.Restaurant;
import com.restaurant.matjip.data.dto.RestaurantListDTO;
import com.restaurant.matjip.data.dto.RestaurantMapDTO;
import com.restaurant.matjip.data.dto.RestaurantSearchRequest;
import com.restaurant.matjip.data.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    /** 리스트 조회 */
    public List<RestaurantListDTO> search(RestaurantSearchRequest request) {
        List<Restaurant> restaurants =
                restaurantRepository.searchByCategories(request.getCategories());

        return restaurants.stream()
                .map(RestaurantListDTO::from)
                .toList();
    }

    /** 지도 조회 */
    public List<RestaurantMapDTO> searchForMap(RestaurantSearchRequest request) {
        List<Restaurant> restaurants =
                restaurantRepository.searchByCategories(request.getCategories());

        return restaurants.stream()
                .map(RestaurantMapDTO::from)
                .toList();
    }
}
