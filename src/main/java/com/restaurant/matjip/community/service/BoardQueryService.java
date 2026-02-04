package com.restaurant.matjip.community.service;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.BoardType;
import com.restaurant.matjip.community.dto.response.BoardDetailResponse;
import com.restaurant.matjip.community.dto.response.BoardListResponse;
import com.restaurant.matjip.community.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardQueryService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<BoardListResponse> getBoards(BoardType type) {
        // ✅ 공지 상단고정 + 최신순 정렬은 백에서 끝
        List<Board> boards = boardRepository.findAllPinnedNoticeFirst(type, BoardType.NOTICE);
        return boards.stream().map(BoardListResponse::from).toList();
    }

    @Transactional
    public BoardDetailResponse getDetail(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. id=" + id));

        // 상세 들어오면 조회수 증가 (원하면 끄면 됨)
        board.increaseViewCount();

        return new BoardDetailResponse(board);
    }
}
