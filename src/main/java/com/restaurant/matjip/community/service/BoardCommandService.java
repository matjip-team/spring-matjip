package com.restaurant.matjip.community.service;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.dto.request.BoardCreateRequest;
import com.restaurant.matjip.community.repository.BoardRepository;
import com.restaurant.matjip.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 게시글 생성/수정/삭제 담당 Service
 */
@Service
@RequiredArgsConstructor
public class BoardCommandService {

    private final BoardRepository boardRepository;

    @Transactional
    public Long create(BoardCreateRequest request, User user) {

        Board board = new Board();
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setBoardType(request.getBoardType());
        board.setUser(user);
        board.setViewCount(0);
        board.setRecommendCount(0);

        return boardRepository.save(board).getId();
    }
}
