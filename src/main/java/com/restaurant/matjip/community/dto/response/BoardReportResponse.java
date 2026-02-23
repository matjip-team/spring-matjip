package com.restaurant.matjip.community.dto.response;

import com.restaurant.matjip.community.domain.BoardReport;
import com.restaurant.matjip.community.domain.ReportActionType;
import com.restaurant.matjip.community.domain.ReportStatus;
import com.restaurant.matjip.community.domain.ReportTargetType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardReportResponse {
    private Long id;
    private Long boardId;
    private String boardTitle;
    private Long commentId;
    private String commentContent;
    private Long reporterId;
    private String reporterNickname;
    private String reason;
    private ReportTargetType targetType;
    private ReportStatus status;
    private ReportActionType actionType;
    private Long processedBy;
    private LocalDateTime processedAt;
    private String processNote;
    private LocalDateTime createdAt;

    public static BoardReportResponse from(BoardReport report) {
        return BoardReportResponse.builder()
                .id(report.getId())
                .boardId(report.getBoard().getId())
                .boardTitle(report.getBoard().getTitle())
                .commentId(report.getComment() != null ? report.getComment().getId() : null)
                .commentContent(report.getComment() != null ? report.getComment().getContent() : null)
                .reporterId(report.getReporter().getId())
                .reporterNickname(report.getReporter().getNickname())
                .reason(report.getReason())
                .targetType(report.getTargetType())
                .status(report.getStatus())
                .actionType(report.getActionType())
                .processedBy(report.getProcessedBy())
                .processedAt(report.getProcessedAt())
                .processNote(report.getProcessNote())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
