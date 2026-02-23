package com.restaurant.matjip.mypage.dto.response;

import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.domain.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponse {

    private String email;
    private Long userId;
    private String name;
    private String nickname;
    private String bio;
    private String profileImageUrl;

    public static UserInfoResponse from(User user, UserProfile profile) {
        return new UserInfoResponse(
                user.getEmail(),
                profile.getUserId(),
                user.getName(),
                profile.getNickname(),
                profile.getBio(),
                profile.getProfileImageUrl()
        );
    }

}
