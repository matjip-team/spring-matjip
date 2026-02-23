package com.restaurant.matjip.community.dto.request;

import com.restaurant.matjip.community.domain.ReportActionType;
import com.restaurant.matjip.community.domain.ReportStatus;
import lombok.Getter;

@Getter
public class ReportProcessRequest {
    private ReportStatus status;
    private ReportActionType action;
    private String note;
}
