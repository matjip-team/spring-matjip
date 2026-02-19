package com.restaurant.matjip.data.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.restaurant.matjip.data.domain.Category;
import com.restaurant.matjip.data.domain.Restaurant;
import com.restaurant.matjip.data.dto.*;
import com.restaurant.matjip.data.repository.CategoryRepository;
import com.restaurant.matjip.data.repository.RestaurantLikeRepository;
import com.restaurant.matjip.data.repository.RestaurantRepository;
import com.restaurant.matjip.data.repository.ReviewRepository;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;

    // 좋아요
    private final RestaurantLikeRepository likeRepository;
    private final UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    /* =====================================================
       🔥 목록 조회 (페이징 + 좋아요 포함)
    ===================================================== */
    public Page<RestaurantListDTO> search(
            RestaurantSearchRequest request,
            int page,
            int size,
            String currentUserEmail
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Restaurant> result = restaurantRepository.search(
                request.getCategories(),
                request.getKeyword(),
                pageable
        );

        return result.map(restaurant -> {

            long likeCount =
                    likeRepository.countByRestaurant_Id(restaurant.getId());

            boolean liked = false;

            if (currentUserEmail != null) {
                User user = userRepository
                        .findByEmail(currentUserEmail)
                        .orElse(null);

                if (user != null) {
                    liked = likeRepository
                            .existsByUser_IdAndRestaurant_Id(
                                    user.getId(),
                                    restaurant.getId()
                            );
                }
            }

            return RestaurantListDTO.from(
                    restaurant,
                    likeCount,
                    liked
            );
        });
    }

    /* =====================================================
       🔥 지도 조회
    ===================================================== */
    public List<RestaurantMapDTO> searchForMap(
            RestaurantSearchRequest request
    ) {

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        Page<Restaurant> result = restaurantRepository.search(
                request.getCategories(),
                request.getKeyword(),
                pageable
        );

        return result.getContent()
                .stream()
                .map(RestaurantMapDTO::from)
                .toList();
    }

    /* =====================================================
       🔥 상세 조회
    ===================================================== */
    @Transactional(readOnly = true)
    public RestaurantDetailDTO getDetail(Long id, String currentUserEmail) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 맛집이 존재하지 않습니다. id=" + id)
                );

        Double avg = reviewRepository.findAverageRating(id);
        double averageRating = avg == null
                ? 0.0
                : Math.round(avg * 10) / 10.0;

        List<ReviewResponse> reviews =
                reviewRepository.findByRestaurant_Id(id)
                        .stream()
                        .map(review ->
                                ReviewResponse.from(review, currentUserEmail)
                        )
                        .toList();

        int reviewCount = reviews.size();

        long likeCount = likeRepository.countByRestaurant_Id(id);

        boolean liked = false;

        if (currentUserEmail != null) {
            User user = userRepository
                    .findByEmail(currentUserEmail)
                    .orElse(null);

            if (user != null) {
                liked = likeRepository
                        .existsByUser_IdAndRestaurant_Id(
                                user.getId(),
                                id
                        );
            }
        }

        return RestaurantDetailDTO.from(
                restaurant,
                averageRating,
                reviewCount,
                reviews,
                likeCount,
                liked
        );
    }

    /* =====================================================
       🔥 Python 수집 기능 (복구 완료)
    ===================================================== */
    @Transactional
    public void collectFromPython() {

        String url = "http://127.0.0.1:8000/collect";

        String rawJson = restTemplate.postForObject(url, null, String.class);

        if (rawJson == null || rawJson.isBlank()) {
            throw new RuntimeException("Python 수집 결과(JSON)가 비어있습니다.");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        PythonCollectResponse response;
        try {
            response = mapper.readValue(rawJson, PythonCollectResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Python 응답 파싱 실패", e);
        }

        if (response.getData() == null) {
            throw new RuntimeException("Python 수집 데이터가 없습니다.");
        }

        for (PythonRestaurantDto dto : response.getData()) {

            if (restaurantRepository.existsByExternalId(dto.getExternalId())) {
                continue;
            }

            Restaurant restaurant = Restaurant.fromPython(
                    dto.getExternalId(),
                    dto.getName(),
                    dto.getAddress(),
                    dto.getLat(),
                    dto.getLng(),
                    dto.getSource()
            );

            restaurant.setImageUrl(dto.getImageUrl());
            restaurant.setPhone(dto.getPhone());
            restaurant.setDescription(dto.getDescription());

            if (dto.getCategory() != null && !dto.getCategory().isBlank()) {

                String[] categoryNames = dto.getCategory().split(">");

                for (String raw : categoryNames) {
                    String name = raw.trim();

                    Category category = categoryRepository
                            .findByName(name)
                            .orElseGet(() ->
                                    categoryRepository.save(
                                            Category.builder()
                                                    .name(name)
                                                    .build()
                                    )
                            );

                    restaurant.addCategory(category);
                }
            }

            restaurantRepository.save(restaurant);
        }
    }
}
