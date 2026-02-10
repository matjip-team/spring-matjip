package com.restaurant.matjip.data.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.restaurant.matjip.data.domain.Category;
import com.restaurant.matjip.data.domain.Restaurant;
import com.restaurant.matjip.data.dto.*;
import com.restaurant.matjip.data.repository.CategoryRepository;
import com.restaurant.matjip.data.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
        Python 수집 기능 (최종본)
       ========================= */

    @Transactional
    public void collectFromPython() {

        String url = "http://127.0.0.1:8000/collect";

        /* 1️⃣ Python 응답을 String(JSON)으로 받기 */
        String rawJson = restTemplate.postForObject(url, null, String.class);

        if (rawJson == null || rawJson.isBlank()) {
            throw new RuntimeException("Python 수집 결과(JSON)가 비어있습니다.");
        }

        /* 2️⃣ ObjectMapper에 snake_case 명시 */
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        PythonCollectResponse response;
        try {
            /* 3️⃣ JSON → DTO 직접 파싱 (핵심) */
            response = mapper.readValue(rawJson, PythonCollectResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Python 응답 파싱 실패", e);
        }

        if (response.getData() == null) {
            throw new RuntimeException("Python 수집 데이터가 없습니다.");
        }

        /* 4️⃣ DB 저장 */
        for (PythonRestaurantDto dto : response.getData()) {

            /* 디버그 로그 (한 번 확인 후 지워도 됨) */
            System.out.println("IMAGE CHECK = " + dto.getImageUrl());

            /* 4-1️⃣ 중복 방지 */
            if (restaurantRepository.existsByExternalId(dto.getExternalId())) {
                continue;
            }

            /* 4-2️⃣ Restaurant 생성 */
            Restaurant restaurant = Restaurant.fromPython(
                    dto.getExternalId(),
                    dto.getName(),
                    dto.getAddress(),
                    dto.getLat(),
                    dto.getLng(),
                    dto.getSource()
            );

            /* 🔥 이미지 URL 저장 (문제 해결 핵심) */
            restaurant.setImageUrl(dto.getImageUrl());

            /* 기타 필드 */
            restaurant.setPhone(dto.getPhone());

            /* 4-3️⃣ 카테고리 매핑 */
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

            /* 4-4️⃣ 저장 */
            restaurantRepository.save(restaurant);
        }
    }
}
