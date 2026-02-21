package com.restaurant.matjip.data.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.restaurant.matjip.data.domain.Category;
import com.restaurant.matjip.data.domain.Restaurant;
import com.restaurant.matjip.data.domain.RestaurantApprovalStatus;
import com.restaurant.matjip.data.dto.*;
import com.restaurant.matjip.data.repository.CategoryRepository;
import com.restaurant.matjip.data.repository.RestaurantLikeRepository;
import com.restaurant.matjip.data.repository.RestaurantRepository;
import com.restaurant.matjip.data.repository.ReviewRepository;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import com.restaurant.matjip.users.constant.UserRole;

import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    private final ReviewRepository reviewRepository;

    // 醫뗭븘??
    private final RestaurantLikeRepository likeRepository;
    private final UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private final RestaurantLicenseFileService restaurantLicenseFileService;

    /* =====================================================
       ?뵦 紐⑸줉 議고쉶 (?섏씠吏?+ 醫뗭븘???ы븿)
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
       ?뵦 吏??議고쉶
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

    @Transactional
    public Long create(RestaurantCreateRequest request, Long userId) {
        User registrant = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName().trim())
                .address(request.getAddress().trim())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .phone(blankToNull(request.getPhone()))
                .description(blankToNull(request.getDescription()))
                .imageUrl(blankToNull(request.getImageUrl()))
                .businessLicenseFileUrl(request.getBusinessLicenseFileKey().trim())
                .source("USER")
                .approvalStatus(RestaurantApprovalStatus.PENDING)
                .registeredBy(registrant)
                .build();

        applyCategories(restaurant, request.getCategoryNames());

        return restaurantRepository.save(restaurant).getId();
    }

    /* =====================================================
       ?뵦 ?곸꽭 議고쉶
    ===================================================== */
    @Transactional(readOnly = true)
    public RestaurantDetailDTO getDetail(Long id, String currentUserEmail) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("?대떦 留쏆쭛??議댁옱?섏? ?딆뒿?덈떎. id=" + id)
                );

        if (restaurant.getApprovalStatus() != RestaurantApprovalStatus.APPROVED) {
            if (currentUserEmail == null) {
                throw new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND);
            }

            User currentUser = userRepository.findByEmail(currentUserEmail).orElse(null);
            boolean isAdmin = currentUser != null && currentUser.getRole() == UserRole.ADMIN;
            if (!isAdmin) {
                throw new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND);
            }
        }
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


    @Transactional
    public void updateApprovalStatus(
            Long restaurantId,
            RestaurantApprovalStatus status,
            String rejectedReason,
            Long adminUserId
    ) {
        assertAdmin(adminUserId);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND));

        if (restaurant.getApprovalStatus() != RestaurantApprovalStatus.PENDING) {
            throw new BusinessException(ErrorCode.RESTAURANT_REQUEST_NOT_PENDING);
        }

        restaurant.setApprovalStatus(status);
        if (status == RestaurantApprovalStatus.REJECTED) {
            if (!StringUtils.hasText(rejectedReason)) {
                throw new IllegalArgumentException("Rejected reason is required when status is REJECTED");
            }
            restaurant.setRejectedReason(rejectedReason.trim());
        } else {
            restaurant.setRejectedReason(null);
        }
        restaurantLicenseFileService.deleteObject(restaurant.getBusinessLicenseFileUrl());
        restaurant.setBusinessLicenseFileUrl(null);
    }

    @Transactional(readOnly = true)
    public String getLicenseViewUrlForAdmin(Long restaurantId, Long adminUserId) {
        assertAdmin(adminUserId);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND));

        String fileKey = restaurant.getBusinessLicenseFileUrl();
        if (fileKey == null || fileKey.isBlank()) {
            throw new IllegalArgumentException("No business license file found");
        }

        return restaurantLicenseFileService.createPresignedViewUrl(fileKey);
    }

    @Transactional(readOnly = true)
    public List<RestaurantAdminListDTO> getByApprovalStatusForAdmin(
            RestaurantApprovalStatus status,
            Long adminUserId
    ) {
        assertAdmin(adminUserId);

        return restaurantRepository.findAllByApprovalStatusAndSourceOrderByCreatedAtDesc(status, "USER")
                .stream()
                .map(restaurant -> RestaurantAdminListDTO.from(
                        restaurant,
                        toRequestImageViewUrl(restaurant.getImageUrl())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public RestaurantAdminDetailDTO getRequestDetailForAdmin(Long restaurantId, Long adminUserId) {
        assertAdmin(adminUserId);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND));

        return RestaurantAdminDetailDTO.from(
                restaurant,
                toRequestImageViewUrl(restaurant.getImageUrl())
        );
    }

    @Transactional(readOnly = true)
    public List<RestaurantMyRequestDTO> getMyRequests(Long userId) {
        return restaurantRepository.findAllByRegisteredByIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(restaurant -> RestaurantMyRequestDTO.from(
                        restaurant,
                        toRequestImageViewUrl(restaurant.getImageUrl())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public RestaurantMyRequestDetailDTO getMyRequestDetail(Long requestId, Long userId) {
        Restaurant restaurant = restaurantRepository.findByIdAndRegisteredById(requestId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND));

        return RestaurantMyRequestDetailDTO.from(
                restaurant,
                toRequestImageViewUrl(restaurant.getImageUrl())
        );
    }

    @Transactional(readOnly = true)
    public String getMyRequestLicenseViewUrl(Long requestId, Long userId) {
        Restaurant restaurant = restaurantRepository.findByIdAndRegisteredById(requestId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND));

        String fileKey = restaurant.getBusinessLicenseFileUrl();
        if (fileKey == null || fileKey.isBlank()) {
            throw new IllegalArgumentException("No business license file found");
        }

        return restaurantLicenseFileService.createPresignedViewUrl(fileKey);
    }

    @Transactional
    public void cancelMyRequest(Long requestId, Long userId) {
        Restaurant restaurant = restaurantRepository.findByIdAndRegisteredById(requestId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND));

        if (restaurant.getApprovalStatus() != RestaurantApprovalStatus.PENDING) {
            throw new BusinessException(ErrorCode.RESTAURANT_REQUEST_NOT_PENDING);
        }

        String fileKey = restaurant.getBusinessLicenseFileUrl();
        if (fileKey != null && !fileKey.isBlank()) {
            restaurantLicenseFileService.deleteObject(fileKey);
        }

        restaurantRepository.delete(restaurant);
    }

    @Transactional
    public void updateMyRequest(Long requestId, RestaurantMyRequestUpdateRequest request, Long userId) {
        Restaurant restaurant = restaurantRepository.findByIdAndRegisteredById(requestId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND));

        if (restaurant.getApprovalStatus() != RestaurantApprovalStatus.PENDING) {
            throw new BusinessException(ErrorCode.RESTAURANT_REQUEST_NOT_PENDING);
        }

        restaurant.setName(request.getName().trim());
        restaurant.setAddress(request.getAddress().trim());
        restaurant.setLatitude(request.getLatitude());
        restaurant.setLongitude(request.getLongitude());
        restaurant.setPhone(blankToNull(request.getPhone()));
        restaurant.setDescription(blankToNull(request.getDescription()));
        restaurant.setImageUrl(blankToNull(request.getImageUrl()));
        String nextBusinessLicenseFileKey = blankToNull(request.getBusinessLicenseFileKey());
        String currentBusinessLicenseFileKey = restaurant.getBusinessLicenseFileUrl();
        if (StringUtils.hasText(nextBusinessLicenseFileKey)
                && !nextBusinessLicenseFileKey.equals(currentBusinessLicenseFileKey)) {
            restaurant.setBusinessLicenseFileUrl(nextBusinessLicenseFileKey);
            restaurantLicenseFileService.deleteObject(currentBusinessLicenseFileKey);
        }

        restaurant.getCategories().clear();
        applyCategories(restaurant, request.getCategoryNames());
    }


    /* =====================================================
       ?뵦 Python ?섏쭛 湲곕뒫 (蹂듦뎄 ?꾨즺)
    ===================================================== */
    @Transactional
    public void collectFromPython() {
        String url = "http://127.0.0.1:8000/collect";

        String rawJson = restTemplate.postForObject(url, null, String.class);

        if (rawJson == null || rawJson.isBlank()) {
            throw new RuntimeException("Python collect response is empty.");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        PythonCollectResponse response;
        try {
            response = mapper.readValue(rawJson, PythonCollectResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Python collect response", e);
        }

        if (response.getData() == null) {
            throw new RuntimeException("Python collect response has no data.");
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
                    dto.getPhone(),
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

                restaurant.setApprovalStatus(RestaurantApprovalStatus.APPROVED);

                if (dto.getCategory() != null && !dto.getCategory().isBlank()) {
                    applyCategories(restaurant, Arrays.stream(dto.getCategory().split(">"))
                            .map(String::trim)
                            .filter(s -> !s.isBlank())
                            .toList());

                }

                restaurantRepository.save(restaurant);
            }
        }
    }

    private void applyCategories(Restaurant restaurant, List<String> categoryNames) {
        if (categoryNames == null) {
            return;
        }

        for (String raw : categoryNames) {
            if (raw == null || raw.isBlank()) {
                continue;
            }

            String name = raw.trim();

            Category category = categoryRepository
                    .findByName(name)
                    .orElseGet(() -> categoryRepository.save(Category.builder().name(name).build()));

            restaurant.addCategory(category);
        }
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private void assertAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRole() != UserRole.ADMIN) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
    }

    private String toRequestImageViewUrl(String imageUrl) {
        if (!StringUtils.hasText(imageUrl)) {
            return null;
        }

        String key = extractS3ObjectKey(imageUrl);
        if (!StringUtils.hasText(key)) {
            return imageUrl.trim();
        }

        try {
            return restaurantLicenseFileService.createPresignedViewUrl(key);
        } catch (Exception ignored) {
            return imageUrl.trim();
        }
    }

    private String extractS3ObjectKey(String imageUrl) {
        String raw = imageUrl == null ? "" : imageUrl.trim();
        if (raw.isBlank()) {
            return null;
        }

        if (raw.startsWith("http://") || raw.startsWith("https://")) {
            try {
                URI uri = URI.create(raw);
                String host = uri.getHost();
                if (host == null || !host.contains("amazonaws.com")) {
                    return null;
                }

                String path = uri.getPath();
                if (path == null || path.isBlank()) {
                    return null;
                }

                return path.startsWith("/") ? path.substring(1) : path;
            } catch (Exception e) {
                return null;
            }
        }

        if (raw.startsWith("/")) {
            return null;
        }

        return raw;
    }
}




