package com.restaurant.matjip.users.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserCreateRequest {


    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String role;   // "USER" or "ADMIN"

    @NotBlank
    private String status; // "ACTIVE", "BLOCKED", "DELETED"
}
