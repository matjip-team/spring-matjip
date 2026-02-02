package com.restaurant.matjip.community.controller;

import com.restaurant.matjip.community.domain.BoardType;
import com.restaurant.matjip.community.dto.request.BoardCreateRequest;
import com.restaurant.matjip.community.dto.response.BoardDetailResponse;
import com.restaurant.matjip.community.dto.response.BoardListResponse;
import com.restaurant.matjip.community.service.BoardCommandService;
import com.restaurant.matjip.community.service.BoardQueryService;
import com.restaurant.matjip.community.service.BoardRecommendationService;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardQueryService boardQueryService;
    private final BoardRecommendationService boardRecommendationService;
    private final BoardCommandService boardCommandService;

    // ✅ JWT 붙기 전 테스트용
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ApiResponse<BoardDetailResponse> getBoardDetail(@PathVariable Long id) {
        return ApiResponse.success(boardQueryService.getDetail(id));
    }

    // ✅ 추천 (테스트용: userId query param)
    @PostMapping("/{id}/recommendations")
    public ApiResponse<Void> recommend(@PathVariable Long id, @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        boardRecommendationService.recommend(id, user);
        return ApiResponse.success(null);
    }

    // ✅ 추천 취소
    @DeleteMapping("/{id}/recommendations")
    public ApiResponse<Void> cancelRecommend(@PathVariable Long id, @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        boardRecommendationService.cancelRecommend(id, user);
        return ApiResponse.success(null);
    }

    // 게시글 목록 조회 API
    @GetMapping
    public ApiResponse<List<BoardListResponse>> getBoards(
            // 말머리(공지/후기) 필터, 없으면 전체 조회
            @RequestParam(required = false) BoardType type
    ) {
        // 조회 로직은 QueryService에서 처리
        return ApiResponse.success(boardQueryService.getBoards(type));
    }

    @PostMapping
    public ApiResponse<Long> createBoard(
            @RequestBody BoardCreateRequest request,
            @RequestParam Long userId // ⚠️ JWT 붙기 전 테스트용
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Long boardId = boardCommandService.create(request, user);
        return ApiResponse.success(boardId);
    }
}
