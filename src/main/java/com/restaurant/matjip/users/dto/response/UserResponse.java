package com.restaurant.matjip.users.dto.response;

import com.restaurant.matjip.users.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String role;
    private String profileImageUrl;
    private String status;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getNickname(),
                user.getRole().name(),
                user.getProfile().getProfileImageUrl(),
                user.getStatus().name()
        );
    }
}
