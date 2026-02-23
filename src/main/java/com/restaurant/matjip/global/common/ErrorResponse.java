package com.restaurant.matjip.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private Object fields; // 필드별 메시지
}
