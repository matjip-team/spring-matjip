package com.restaurant.matjip.mypage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequest {

    @NotBlank(message = "{error.validation.profile.nickname.length}")
    @Size(max = 255)
    private String nickname;

    @Size(min = 8, max = 100, message = "{error.validation.user.password.length}") // 해시 길이 기준 고려, 최소 8자
    private String password;

    @Size(max = 4000, message = "{error.validation.profile.bio.length}")
    private String bio;

    @Size(max = 500)
    private String profileImageUrl;

    private MultipartFile profileImage;

}
