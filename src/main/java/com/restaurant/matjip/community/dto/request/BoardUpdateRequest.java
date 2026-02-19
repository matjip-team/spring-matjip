package com.restaurant.matjip.community.dto.request;

import com.restaurant.matjip.community.domain.BoardType;
import lombok.Getter;

@Getter
public class BoardUpdateRequest {

    private String title;
    private String content;

    private String contentHtml;
    private String contentDelta;
    private BoardType boardType;
}


