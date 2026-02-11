package com.restaurant.matjip.community.service;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.dto.request.BoardCreateRequest;
import com.restaurant.matjip.community.dto.request.BoardUpdateRequest;
import com.restaurant.matjip.community.repository.BoardRepository;
import com.restaurant.matjip.community.repository.BoardViewRepository;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardCommandService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardViewRepository boardViewRepository;

    /* ================== 게시글 생성 ================== */

    @Transactional
    public Long create(BoardCreateRequest request, Long userId) {

        // 작성자 조회
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자 없음"));

        // 게시글 생성
        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .boardType(request.getBoardType())
                .user(writer)
                .viewCount(0)
                .recommendCount(0)
                .build();

        // 저장 후 ID 반환
        return boardRepository.save(board).getId();
    }

    /* ================== 게시글 수정 ================== */
    @Transactional
    public void update(Long boardId, Long userId, BoardUpdateRequest req) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow();

        if (!board.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한 없음");
        }

        board.setTitle(req.getTitle());
        board.setContent(req.getContent());
        board.setBoardType(req.getBoardType());
    }

    /* ================== 게시글 삭제 ================== */
    @Transactional
    public void delete(Long boardId, Long userId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow();

        // 작성자만 삭제 가능
        if (!board.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한 없음");
        }

        // ✅ 조회수 기록 먼저 삭제
        boardViewRepository.deleteByBoardId(boardId);

        // ✅ 게시글 삭제
        boardRepository.delete(board);
    }

}