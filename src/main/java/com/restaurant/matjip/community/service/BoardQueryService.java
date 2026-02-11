package com.restaurant.matjip.community.service;

import com.restaurant.matjip.community.controller.enums.BoardSearchType;
import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.BoardType;
import com.restaurant.matjip.community.domain.BoardView;
import com.restaurant.matjip.community.dto.response.BoardDetailResponse;
import com.restaurant.matjip.community.dto.response.BoardListResponse;
import com.restaurant.matjip.community.dto.response.BoardPageResponse;
import com.restaurant.matjip.community.repository.BoardRecommendationRepository;
import com.restaurant.matjip.community.repository.BoardRepository;
import com.restaurant.matjip.community.repository.BoardViewRepository;
import com.restaurant.matjip.community.repository.CommentRepository;
import com.restaurant.matjip.global.common.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardQueryService {

    private final BoardRepository boardRepository;
    private final BoardViewRepository boardViewRepository;
    private final BoardRecommendationRepository boardRecommendationRepository;
    private final CommentRepository commentRepository;

    /* ================== 게시글 목록 조회 ================== */

    @Transactional(readOnly = true)
    public BoardPageResponse getBoards(
            BoardType type,
            String keyword,
            BoardSearchType searchType,
            Pageable pageable
    ) {

        List<BoardListResponse> notices =
                boardRepository.findByBoardTypeOrderByIdDesc(BoardType.NOTICE)
                        .stream()
                        .map(board -> {
                            int count = commentRepository.countByBoardIdAndDeletedFalse(board.getId());
                            return BoardListResponse.from(board, count);
                        })
                        .toList();

        Page<Board> page;

        switch (searchType) {
            case TITLE ->
                    page = boardRepository.searchTitle(type, keyword, pageable);

            case CONTENT ->
                    page = boardRepository.searchContent(type, keyword, pageable);

            case AUTHOR ->
                    page = boardRepository.searchAuthor(type, keyword, pageable);

            case COMMENT ->
                    page = boardRepository.searchComment(type, keyword, pageable);

            default ->
                    page = boardRepository.searchNormalBoards(type, keyword, pageable);
        }

        Page<BoardListResponse> normalPage =
                page.map(board -> {
                    int count = commentRepository.countByBoardIdAndDeletedFalse(board.getId());
                    return BoardListResponse.from(board, count);
                });
        return new BoardPageResponse(notices, normalPage);
    }


    /* ================== 게시글 상세 조회 + 조회수 처리 ================== */

    @Transactional
    public BoardDetailResponse getDetail(Long boardId, CustomUserDetails userDetails) {

        // 게시글 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow();

        // 🔹 비로그인 유저 → 그냥 조회수 증가
        if (userDetails == null) {
            board.increaseViewCount();
            int commentCount = commentRepository.countByBoardIdAndDeletedFalse(boardId);
            return new BoardDetailResponse(board, false, commentCount);
        }

        Long userId = userDetails.getId();

        // 🔹 로그인 유저 + 처음 보는 글일 때만 조회수 증가
        if (!boardViewRepository.existsByBoardIdAndUserId(boardId, userId)) {

            BoardView view = BoardView.builder()
                    .board(board)
                    .userId(userId)
                    .viewedAt(LocalDateTime.now())
                    .build();

            boardViewRepository.save(view);
            board.increaseViewCount();
        }

        boolean recommended =
                boardRecommendationRepository.existsByBoardIdAndUserId(boardId, userId);

        int commentCount = commentRepository.countByBoardIdAndDeletedFalse(boardId);
        return new BoardDetailResponse(board, recommended, commentCount);
    }
}

