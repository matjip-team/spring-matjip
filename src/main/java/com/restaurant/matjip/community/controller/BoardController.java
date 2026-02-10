package com.restaurant.matjip.community.controller;

import com.restaurant.matjip.community.domain.BoardType;
import com.restaurant.matjip.community.dto.request.BoardCreateRequest;
import com.restaurant.matjip.community.dto.response.BoardDetailResponse;
import com.restaurant.matjip.community.dto.response.BoardListResponse;
import com.restaurant.matjip.community.service.BoardCommandService;
import com.restaurant.matjip.community.service.BoardQueryService;
import com.restaurant.matjip.community.service.BoardRecommendationService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardQueryService boardQueryService;
    private final BoardRecommendationService boardRecommendationService;
    private final BoardCommandService boardCommandService;

    // 게시글 단건 조회
    @GetMapping("/{id}")
    public ApiResponse<BoardDetailResponse> getBoardDetail(@PathVariable Long id) {
        return ApiResponse.success(boardQueryService.getDetail(id));
    }

    // 게시글 목록 조회
    @GetMapping
    public ApiResponse<List<BoardListResponse>> getBoards(
            @RequestParam(required = false) BoardType type
    ) {
        return ApiResponse.success(boardQueryService.getBoards(type));
    }

    // 게시글 작성 (JWT 기반)
    @PostMapping
    public ApiResponse<Long> createBoard(
            @RequestBody BoardCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new IllegalStateException("인증된 사용자 정보가 없습니다.");
        }

        Long boardId = boardCommandService.create(request, userDetails.getId());
        return ApiResponse.success(boardId);
    }

    // 추천
    @PostMapping("/{id}/recommendations")
    public ApiResponse<Void> recommend(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        boardRecommendationService.recommend(id, user);
        return ApiResponse.success(null);
    }

    // 추천 취소
    @DeleteMapping("/{id}/recommendations")
    public ApiResponse<Void> cancelRecommend(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        boardRecommendationService.cancelRecommend(id, user);
        return ApiResponse.success(null);
    }
}
