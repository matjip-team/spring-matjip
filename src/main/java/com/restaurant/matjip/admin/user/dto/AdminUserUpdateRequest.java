package com.restaurant.matjip.admin.user.dto;

import com.restaurant.matjip.users.constant.UserRole;
import com.restaurant.matjip.users.constant.UserStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserUpdateRequest {
    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @NotBlank(message = "닉네임은 필수입니다")
    private String nickname;

    private UserRole role;
    private UserStatus status;
    private String bio;
}
