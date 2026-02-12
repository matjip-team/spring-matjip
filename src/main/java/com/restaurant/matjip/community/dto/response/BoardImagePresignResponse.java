package com.restaurant.matjip.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardImagePresignResponse {

    private String uploadUrl;
    private String fileUrl;
    private String method;
}
