package com.restaurant.matjip.community.controller;

import com.restaurant.matjip.community.domain.BoardType;
import com.restaurant.matjip.community.dto.request.BoardCreateRequest;
import com.restaurant.matjip.community.controller.enums.BoardSearchType;
import com.restaurant.matjip.community.dto.request.BoardUpdateRequest;
import com.restaurant.matjip.community.dto.response.BoardDetailResponse;
import com.restaurant.matjip.community.dto.response.BoardPageResponse;
import com.restaurant.matjip.community.service.BoardCommandService;
import com.restaurant.matjip.community.service.BoardQueryService;
import com.restaurant.matjip.community.service.BoardRecommendationService;
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

    /* ===================== 게시글 목록 ===================== */

//    @GetMapping
//    public ApiResponse<BoardPageResponse> getBoards(
//            @RequestParam(required = false) BoardType type,
//            @RequestParam(required = false) String keyword,
//            Pageable pageable
//    ) {
//        return ApiResponse.success(
//                boardQueryService.getBoards(type, keyword, pageable)
//        );
//    }

    /* ===================== 게시글 검색 ===================== */

    @GetMapping
    public ApiResponse<BoardPageResponse> getBoards(
            @RequestParam(required = false) BoardType type,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "TITLE_CONTENT") BoardSearchType searchType,
            Pageable pageable
    ) {
        return ApiResponse.success(boardQueryService.getBoards(type, keyword, searchType, pageable));
    }

    /* ===================== 게시글 작성 ===================== */

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

    /* ===================== 게시글 수정 ===================== */
    @PutMapping("/{id}")
    public ApiResponse<Void> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardUpdateRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        boardCommandService.update(id, user.getId(), req);
        return ApiResponse.success(null);
    }

    /* ===================== 게시글 삭제 ===================== */

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

    /* ===================== 추천 ===================== */

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

    /* ===================== 게시글 상세 ===================== */

    @GetMapping("/{id}")
    public ApiResponse<BoardDetailResponse> getBoardDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.success(
                boardQueryService.getDetail(id, userDetails)
        );
    }
}
