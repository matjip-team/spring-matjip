package com.restaurant.matjip.community.service;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.BoardType;
import com.restaurant.matjip.community.dto.response.BoardDetailResponse;
import com.restaurant.matjip.community.dto.response.BoardListResponse;
import com.restaurant.matjip.community.repository.BoardRepository;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardQueryService {

    public BoardDetailResponse getDetail(Long id) {

        // 게시글 조회 (없으면 예외)
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));

        // Entity → 상세 응답 DTO 변환
        return new BoardDetailResponse(board);
    }

    private final BoardRepository boardRepository;

    // 게시글 목록 조회
    public List<BoardListResponse> getBoards(BoardType boardType) {

        // 말머리 조건 있으면 필터, 없으면 전체 조회
        List<Board> boards = (boardType == null)
                ? boardRepository.findAllByOrderByIdDesc()
                : boardRepository.findByBoardTypeOrderByIdDesc(boardType);

        // Entity → 목록 응답 DTO 변환
        return boards.stream()
                .map(BoardListResponse::from)
                .toList();
    }
}
