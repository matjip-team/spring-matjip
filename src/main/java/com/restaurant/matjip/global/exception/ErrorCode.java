package com.restaurant.matjip.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //ErrorCode는 비즈니스/공통 오류만
    //Validation 메시지는 messages.properties + fields Map

    // Validation
    VALIDATION_ERROR("error.common.validation", HttpStatus.BAD_REQUEST),

    // User
    USER_NOT_FOUND("error.user.not-found", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL("error.user.duplicate-email", HttpStatus.CONFLICT),

    // Common
    INTERNAL_ERROR("error.common.internal", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED_ERROR("error.common.unauthorized", HttpStatus.UNAUTHORIZED),

    // Board
    BOARD_NOT_FOUND("error.baord.not-found", HttpStatus.NOT_FOUND);

    private final String messageKey;
    private final HttpStatus status;
}

