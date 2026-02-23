package com.restaurant.matjip.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank(message = "{error.validation.user.email.required}")
    @Email(message = "{error.validation.user.email.invalid}")
    @Size(max = 100) // DB 컬럼과 동일하게
    private String email;

    @NotBlank
    @Size(min = 8, max = 100, message = "{error.validation.user.password.length}") // 해시 길이 기준 고려, 최소 8자
    private String password;

}
