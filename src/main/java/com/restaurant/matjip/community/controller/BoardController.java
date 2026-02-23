package com.restaurant.matjip.community.controller;

import com.restaurant.matjip.community.controller.enums.BoardSearchType;
import com.restaurant.matjip.community.domain.BoardType;
import com.restaurant.matjip.community.dto.request.BoardCreateRequest;
import com.restaurant.matjip.community.dto.request.BoardUpdateRequest;
import com.restaurant.matjip.community.dto.request.ReportCreateRequest;
import com.restaurant.matjip.community.dto.response.BoardDetailResponse;
import com.restaurant.matjip.community.dto.response.BoardPageResponse;
import com.restaurant.matjip.community.service.BoardCommandService;
import com.restaurant.matjip.community.service.BoardQueryService;
import com.restaurant.matjip.community.service.BoardRecommendationService;
import com.restaurant.matjip.community.service.BoardReportService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardQueryService boardQueryService;
    private final BoardRecommendationService boardRecommendationService;
    private final BoardCommandService boardCommandService;
    private final BoardReportService boardReportService;
    @GetMapping
    public ApiResponse<BoardPageResponse> getBoards(
            @RequestParam(required = false) BoardType type,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "TITLE_CONTENT") BoardSearchType searchType,
            Pageable pageable
    ) {
        return ApiResponse.success(boardQueryService.getBoards(type, keyword, searchType, pageable));
    }

    @PostMapping
    public ApiResponse<Long> createBoard(
            @Valid @RequestBody BoardCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        Long boardId = boardCommandService.create(request, userDetails.getId());
        return ApiResponse.success(boardId);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardUpdateRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        boardCommandService.update(id, user.getId(), req);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        boardCommandService.delete(id, userDetails.getId());
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/recommendations")
    public ApiResponse<Void> recommend(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        boardRecommendationService.toggleRecommend(id, userDetails.getId());
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/reports")
    public ApiResponse<Void> reportBoard(
            @PathVariable Long id,
            @Valid @RequestBody ReportCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        boardReportService.reportBoard(id, userDetails.getId(), request);
        return ApiResponse.success("신고가 접수되었습니다.", null);
    }

    @GetMapping("/{id}")
    public ApiResponse<BoardDetailResponse> getBoardDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.success(boardQueryService.getDetail(id, userDetails));
    }
}
