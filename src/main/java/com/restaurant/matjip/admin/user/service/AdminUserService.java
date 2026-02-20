package com.restaurant.matjip.admin.user.service;

import com.restaurant.matjip.admin.user.dto.AdminUserDetailDto;
import com.restaurant.matjip.admin.user.dto.AdminUserListResponse;
import com.restaurant.matjip.admin.user.dto.AdminUserUpdateRequest;
import com.restaurant.matjip.admin.user.repository.AdminUserRepository;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import com.restaurant.matjip.users.constant.UserStatus;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.domain.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;

    public AdminUserListResponse findAll(Pageable pageable, String keyword, String searchType, String statusParam) {
        UserStatus status = parseStatus(statusParam);
        Page<User> page;
        if (keyword != null && !keyword.isBlank()) {
            page = adminUserRepository.searchUsers(keyword, searchType, status, pageable);
        } else {
            page = status != null
                    ? adminUserRepository.findAllByStatusOrderByCreatedAtDesc(status, pageable)
                    : adminUserRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
        List<AdminUserDetailDto> content = page.getContent().stream()
                .map(this::toDetailDto)
                .collect(Collectors.toList());
        return AdminUserListResponse.builder()
                .content(content)
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .size(page.getSize())
                .number(page.getNumber())
                .build();
    }

    public AdminUserDetailDto findById(Long id) {
        User user = adminUserRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return toDetailDto(user);
    }

    @Transactional
    public void update(Long id, AdminUserUpdateRequest request) {
        User user = adminUserRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.setRole(request.getRole());
        if (request.getStatus() != null) {
            user.changeStatus(request.getStatus());
        }

        UserProfile userProfile = user.getUserProfile();
        userProfile.setNickname(request.getNickname());
        userProfile.setBio(request.getBio());
    }

    private UserStatus parseStatus(String statusParam) {
        if (statusParam == null || statusParam.isBlank()) return null;
        try {
            return UserStatus.valueOf(statusParam.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private AdminUserDetailDto toDetailDto(User u) {
        return AdminUserDetailDto.builder()
                .id(u.getId())
                .email(u.getEmail())
                .name(u.getName())
                .nickname(u.getNickname())
                .role(u.getRole().name())
                .status(u.getStatus() != null ? u.getStatus().name() : null)
                .profileImageUrl(u.getUserProfile().getProfileImageUrl())
                .bio(u.getUserProfile().getBio())
                .createdAt(u.getCreatedAt() != null ? u.getCreatedAt().toString() : null)
                .updatedAt(u.getUpdatedAt() != null ? u.getUpdatedAt().toString() : null)
                .build();
    }
}
