package com.restaurant.matjip.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlogImagePresignResponse {

    private String uploadUrl;
    private String fileUrl;
    private String method;
}


