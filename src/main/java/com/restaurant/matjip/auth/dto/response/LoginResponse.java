package com.restaurant.matjip.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {
    private long id;
    private String email;
    private String name;
    private String nickname;
    private String profileImageUrl;
    private List<String> roles;
}
