package com.restaurant.matjip.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReportCreateRequest {

    @NotBlank(message = "신고 사유를 입력해주세요.")
    private String reason;
}
