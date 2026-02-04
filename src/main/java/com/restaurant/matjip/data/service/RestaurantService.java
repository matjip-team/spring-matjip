package com.restaurant.matjip.data.service;

import com.restaurant.matjip.data.domain.Category;
import com.restaurant.matjip.data.domain.Restaurant;
import com.restaurant.matjip.data.dto.*;
import com.restaurant.matjip.data.repository.CategoryRepository;
import com.restaurant.matjip.data.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    /* =========================
       기존 조회 기능 (유지)
       ========================= */

    public List<RestaurantListDTO> search(RestaurantSearchRequest request) {
        return restaurantRepository.searchByCategories(request.getCategories())
                .stream()
                .map(RestaurantListDTO::from)
                .toList();
    }

    public List<RestaurantMapDTO> searchForMap(RestaurantSearchRequest request) {
        return restaurantRepository.searchByCategories(request.getCategories())
                .stream()
                .map(RestaurantMapDTO::from)
                .toList();
    }

    /* =========================
       ⭐ Python 수집 기능
       ========================= */

    @Transactional
    public void collectFromPython() {

        String url = "http://127.0.0.1:8000/collect";

        PythonCollectResponse response =
                restTemplate.postForObject(url, null, PythonCollectResponse.class);

        if (response == null || response.getData() == null) {
            throw new RuntimeException("Python 수집 결과가 비어있습니다.");
        }

        for (PythonRestaurantDto dto : response.getData()) {

            /* 1️⃣ 중복 방지 */
            if (restaurantRepository.existsByExternalId(dto.getExternal_id())) {
                continue;
            }

            /* 2️⃣ Restaurant 생성 (여기서 위경도 이미 세팅됨) */
            Restaurant restaurant = Restaurant.fromPython(
                    dto.getExternal_id(),
                    dto.getName(),
                    dto.getAddress(),
                    dto.getLat(),
                    dto.getLng(),
                    dto.getSource()
            );

            /* 이미지 기능 추가 (핵심 한 줄) */
            restaurant.setImageUrl(dto.getImage_url());

            /* 기타 필드 */
            restaurant.setPhone(dto.getPhone());

            /* 3️⃣ 카테고리 매핑 */
            if (dto.getCategory() != null && !dto.getCategory().isBlank()) {

                String[] categoryNames = dto.getCategory().split(">");

                for (String raw : categoryNames) {
                    String name = raw.trim();

                    Category category = categoryRepository
                            .findByName(name)
                            .orElseGet(() ->
                                    categoryRepository.save(
                                            Category.builder().name(name).build()
                                    )
                            );

                    restaurant.addCategory(category);
                }
            }

            /* 4️⃣ 저장 */
            restaurantRepository.save(restaurant);
        }
    }
}
