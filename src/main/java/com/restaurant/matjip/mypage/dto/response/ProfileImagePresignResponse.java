package com.restaurant.matjip.mypage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileImagePresignResponse {
    private String uploadUrl;
    private String fileUrl;
    private String method;
}
