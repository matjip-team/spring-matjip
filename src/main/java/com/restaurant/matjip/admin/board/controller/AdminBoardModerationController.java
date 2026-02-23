package com.restaurant.matjip.admin.board.controller;

import com.restaurant.matjip.admin.board.service.AdminBoardCommandService;
import com.restaurant.matjip.admin.board.util.AdminBoardAuthChecker;
import com.restaurant.matjip.community.domain.ReportStatus;
import com.restaurant.matjip.community.dto.request.ReportProcessRequest;
import com.restaurant.matjip.community.dto.response.BoardReportResponse;
import com.restaurant.matjip.community.service.BoardReportService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/boards")
@RequiredArgsConstructor
public class AdminBoardModerationController {

    private final AdminBoardCommandService adminBoardCommandService;
    private final BoardReportService boardReportService;

    @PatchMapping("/{id}/hide")
    public ApiResponse<Void> hideBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AdminBoardAuthChecker.requireAdmin(userDetails);
        adminBoardCommandService.hide(id);
        return ApiResponse.success(null);
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AdminBoardAuthChecker.requireAdmin(userDetails);
        adminBoardCommandService.restore(id);
        return ApiResponse.success(null);
    }

    @PatchMapping("/{id}/pin")
    public ApiResponse<Void> pinBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AdminBoardAuthChecker.requireAdmin(userDetails);
        adminBoardCommandService.pinNotice(id);
        return ApiResponse.success(null);
    }

    @PatchMapping("/{id}/unpin")
    public ApiResponse<Void> unpinBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AdminBoardAuthChecker.requireAdmin(userDetails);
        adminBoardCommandService.unpin(id);
        return ApiResponse.success(null);
    }

    @GetMapping("/reports")
    public ApiResponse<Page<BoardReportResponse>> getReports(
            @RequestParam(required = false) ReportStatus status,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AdminBoardAuthChecker.requireAdmin(userDetails);
        return ApiResponse.success(boardReportService.getReports(status, pageable));
    }

    @PatchMapping("/reports/{reportId}")
    public ApiResponse<Void> processReport(
            @PathVariable Long reportId,
            @Valid @RequestBody ReportProcessRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AdminBoardAuthChecker.requireAdmin(userDetails);
        boardReportService.processReport(reportId, userDetails.getId(), request);
        return ApiResponse.success(null);
    }
}
