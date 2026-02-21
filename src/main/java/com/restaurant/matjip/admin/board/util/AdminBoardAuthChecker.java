package com.restaurant.matjip.admin.board.util;

import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

public final class AdminBoardAuthChecker {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private AdminBoardAuthChecker() {
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
