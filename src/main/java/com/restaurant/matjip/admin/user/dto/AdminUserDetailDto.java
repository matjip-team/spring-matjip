package com.restaurant.matjip.admin.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDetailDto {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String role;
    private String status;
    private String profileImageUrl;
    private String bio;
    private String createdAt;
    private String updatedAt;
}
