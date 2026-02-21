package com.restaurant.matjip.community.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import com.restaurant.matjip.users.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "board_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private ReportTargetType targetType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReportStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", length = 30)
    private ReportActionType actionType;

    @Column(name = "processed_by")
    private Long processedBy;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "process_note", columnDefinition = "TEXT")
    private String processNote;

    public boolean isPending() {
        return this.status == ReportStatus.PENDING;
    }

    public void markAccepted(Long adminId, ReportActionType actionType, String note) {
        this.status = ReportStatus.ACCEPTED;
        this.actionType = actionType;
        this.processedBy = adminId;
        this.processedAt = LocalDateTime.now();
        this.processNote = note;
    }

    public void markRejected(Long adminId, String note) {
        this.status = ReportStatus.REJECTED;
        this.actionType = null;
        this.processedBy = adminId;
        this.processedAt = LocalDateTime.now();
        this.processNote = note;
    }
}
