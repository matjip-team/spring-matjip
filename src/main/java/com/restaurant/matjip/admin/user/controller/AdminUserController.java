package com.restaurant.matjip.admin.user.controller;

import com.restaurant.matjip.admin.user.dto.AdminUserDetailDto;
import com.restaurant.matjip.admin.user.dto.AdminUserListResponse;
import com.restaurant.matjip.admin.user.dto.AdminUserUpdateRequest;
import com.restaurant.matjip.admin.user.service.AdminUserService;
import com.restaurant.matjip.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 관리 API (ROLE_ADMIN 전용)
 */
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ApiResponse<AdminUserListResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "EMAIL") String searchType,
            @RequestParam(required = false) String status) {
        var pageable = PageRequest.of(page, size);
        var result = adminUserService.findAll(pageable, keyword, searchType, status);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    public ApiResponse<AdminUserDetailDto> detail(@PathVariable Long id) {
        var user = adminUserService.findById(id);
        return ApiResponse.success(user);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(
            @PathVariable Long id,
            @Valid @RequestBody AdminUserUpdateRequest request) {
        adminUserService.update(id, request);
        return ApiResponse.success(null);
    }
}
