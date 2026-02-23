package com.restaurant.matjip.data.controller;

import com.restaurant.matjip.data.service.RestaurantService;
import com.restaurant.matjip.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminCollectController {

    private final RestaurantService restaurantService;

    /**
     * Python 수집 서버 호출 → DB 저장
     */
    @PostMapping("/collect")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> collect() {

        restaurantService.collectFromPython();

        return ApiResponse.success("맛집 수집이 완료되었습니다.");
    }
}
