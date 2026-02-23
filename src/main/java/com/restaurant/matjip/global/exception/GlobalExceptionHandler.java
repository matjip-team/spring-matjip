package com.restaurant.matjip.global.exception;

import com.restaurant.matjip.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler({
            BusinessException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException e,
            Locale locale
    ) {
        String message = messageSource.getMessage(
                e.getErrorCode().getMessageKey(),
                null,
                locale
        );

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.fail(
                        e.getErrorCode().name(),
                        message
                ));
    }

    // Validation Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException e,
            Locale locale
    ) {

        List<Map<String, Object>> fields = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ))
                .entrySet()
                .stream()
                .map(entry -> Map.of(
                        "field", entry.getKey(),
                        "messages", entry.getValue()
                ))
                .toList();

        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getStatus())
                .body(ApiResponse.fail(
                        ErrorCode.VALIDATION_ERROR.name(),
                        messageSource.getMessage(
                                ErrorCode.VALIDATION_ERROR.getMessageKey(),
                                null,
                                locale
                        ),
                        fields
                ));
    }

    // 예상 못 한 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
            Exception e,
            Locale locale
    ) {
        String message = messageSource.getMessage(
                ErrorCode.INTERNAL_ERROR.getMessageKey(),
                null,
                locale
        );

        return ResponseEntity
                .status(ErrorCode.INTERNAL_ERROR.getStatus())
                .body(ApiResponse.fail(
                        ErrorCode.INTERNAL_ERROR.name(),
                        message + ":" + e.getMessage()
                ));
    }

}
