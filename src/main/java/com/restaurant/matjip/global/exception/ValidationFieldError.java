package com.restaurant.matjip.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationFieldError {
    private String field;
    private String message;
}
