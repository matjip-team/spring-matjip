package com.restaurant.matjip.community.service;

import com.restaurant.matjip.community.domain.*;
import com.restaurant.matjip.community.dto.request.ReportCreateRequest;
import com.restaurant.matjip.community.dto.request.ReportProcessRequest;
import com.restaurant.matjip.community.dto.response.BoardReportResponse;
import com.restaurant.matjip.community.repository.BoardReportRepository;
import com.restaurant.matjip.community.repository.BoardRepository;
import com.restaurant.matjip.community.repository.CommentRepository;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardReportService {

    private final BoardReportRepository boardReportRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void reportBoard(Long boardId, Long reporterId, ReportCreateRequest req) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));

        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (boardReportRepository.existsByReporterIdAndBoardIdAndCommentIsNull(reporterId, boardId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_REPORT);
        }

        BoardReport report = BoardReport.builder()
                .board(board)
                .comment(null)
                .reporter(reporter)
                .targetType(ReportTargetType.BOARD)
                .reason(req.getReason().trim())
                .status(ReportStatus.PENDING)
                .build();

        board.increaseReportCount();
        boardReportRepository.save(report);
    }

    @Transactional
    public void reportComment(Long boardId, Long commentId, Long reporterId, ReportCreateRequest req) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));

        Comment comment = commentRepository.findByIdAndBoardId(commentId, boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (boardReportRepository.existsByReporterIdAndCommentId(reporterId, commentId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_REPORT);
        }

        BoardReport report = BoardReport.builder()
                .board(board)
                .comment(comment)
                .reporter(reporter)
                .targetType(ReportTargetType.COMMENT)
                .reason(req.getReason().trim())
                .status(ReportStatus.PENDING)
                .build();

        comment.increaseReportCount();
        board.increaseReportCount();
        boardReportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public Page<BoardReportResponse> getReports(ReportStatus status, Pageable pageable) {
        Page<BoardReport> page = status == null
                ? boardReportRepository.findAllByOrderByIdDesc(pageable)
                : boardReportRepository.findByStatusOrderByIdDesc(status, pageable);

        return page.map(BoardReportResponse::from);
    }

    @Transactional
    public void processReport(Long reportId, Long adminId, ReportProcessRequest req) {
        BoardReport report = boardReportRepository.findById(reportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_NOT_FOUND));

        if (!report.isPending()) {
            throw new BusinessException(ErrorCode.REPORT_ALREADY_PROCESSED);
        }

        if (req.getStatus() == null) {
            throw new BusinessException(ErrorCode.INVALID_REPORT_ACTION);
        }

        if (req.getStatus() == ReportStatus.ACCEPTED) {
            processAccepted(report, adminId, req);
        } else if (req.getStatus() == ReportStatus.REJECTED) {
            report.markRejected(adminId, req.getNote());
        } else {
            throw new BusinessException(ErrorCode.INVALID_REPORT_ACTION);
        }

        decreasePendingCounters(report);
    }

    private void processAccepted(BoardReport report, Long adminId, ReportProcessRequest req) {
        if (req.getAction() == null) {
            throw new BusinessException(ErrorCode.INVALID_REPORT_ACTION);
        }

        if (req.getAction() == ReportActionType.HIDE_BOARD) {
            report.getBoard().hide();
            report.markAccepted(adminId, ReportActionType.HIDE_BOARD, req.getNote());
            return;
        }

        if (req.getAction() == ReportActionType.DELETE_COMMENT) {
            if (report.getComment() == null) {
                throw new BusinessException(ErrorCode.INVALID_REPORT_ACTION);
            }
            report.getComment().softDelete();
            report.markAccepted(adminId, ReportActionType.DELETE_COMMENT, req.getNote());
            return;
        }

        throw new BusinessException(ErrorCode.INVALID_REPORT_ACTION);
    }

    private void decreasePendingCounters(BoardReport report) {
        Board board = report.getBoard();
        board.decreaseReportCount();

        if (report.getComment() != null) {
            report.getComment().decreaseReportCount();
        }
    }
}
