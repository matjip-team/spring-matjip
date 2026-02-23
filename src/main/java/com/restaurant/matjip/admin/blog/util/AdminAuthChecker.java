package com.restaurant.matjip.admin.blog.util;

import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

/**
 * 관리자 전용 API 접근 시 ADMIN 역할 검증
 */
public final class AdminAuthChecker {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private AdminAuthChecker() {
    }

    public static void requireAdmin(CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        boolean isAdmin = userDetails.getAuthorities() != null
                && userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> Objects.equals(auth, ROLE_ADMIN));

        if (!isAdmin) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
    }
}
