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
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardQueryService {

    private final BoardRepository boardRepository;
    private final BoardViewRepository boardViewRepository;
    private final BoardRecommendationRepository boardRecommendationRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public BoardPageResponse getBoards(
            BoardType type,
            String keyword,
            BoardSearchType searchType,
            Pageable pageable
    ) {

        var notices = boardRepository.findByBoardTypeOrderByIdDesc(BoardType.NOTICE)
                .stream()
                .map(board -> {
                    int count = commentRepository.countByBoardIdAndDeletedFalse(board.getId());
                    return BoardListResponse.from(board, count);
                })
                .toList();

        Page<Board> page;

        switch (searchType) {
            case TITLE -> page = boardRepository.searchTitle(type, keyword, pageable);
            case CONTENT -> page = boardRepository.searchContent(type, keyword, pageable);
            case AUTHOR -> page = boardRepository.searchAuthor(type, keyword, pageable);
            case COMMENT -> page = boardRepository.searchComment(type, keyword, pageable);
            default -> page = boardRepository.searchNormalBoards(type, keyword, pageable);
        }

        var normalPage = page.map(board -> {
            int count = commentRepository.countByBoardIdAndDeletedFalse(board.getId());
            return BoardListResponse.from(board, count);
        });

        return new BoardPageResponse(notices, normalPage);
    }

    @Transactional
    public BoardDetailResponse getDetail(Long boardId, CustomUserDetails userDetails) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));

        if (board.isHidden() && !canAccessHiddenBoard(board, userDetails)) {
            throw new BusinessException(ErrorCode.BOARD_NOT_FOUND);
        }

        if (userDetails == null) {
            board.increaseViewCount();
            int commentCount = commentRepository.countByBoardIdAndDeletedFalse(boardId);
            return new BoardDetailResponse(board, false, commentCount);
        }

        Long userId = userDetails.getId();

        if (!boardViewRepository.existsByBoardIdAndUserId(boardId, userId)) {
            BoardView view = BoardView.builder()
                    .board(board)
                    .userId(userId)
                    .viewedAt(LocalDateTime.now())
                    .build();

            boardViewRepository.save(view);
            board.increaseViewCount();
        }

        boolean recommended = boardRecommendationRepository.existsByBoardIdAndUserId(boardId, userId);

        int commentCount = commentRepository.countByBoardIdAndDeletedFalse(boardId);
        return new BoardDetailResponse(board, recommended, commentCount);
    }

    private boolean canAccessHiddenBoard(Board board, CustomUserDetails userDetails) {
        if (userDetails == null) {
            return false;
        }

        if (board.getUser().getId().equals(userDetails.getId())) {
            return true;
        }

        return userDetails.getAuthorities() != null
                && userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }
}
