package com.restaurant.matjip.community.service;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.BoardRecommendation;
import com.restaurant.matjip.community.repository.BoardRepository;
import com.restaurant.matjip.community.repository.BoardRecommendationRepository;
import com.restaurant.matjip.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardRecommendationService {

    private final BoardRepository boardRepository;
    private final BoardRecommendationRepository boardRecommendationRepository;

    @Transactional
    public void recommend(Long boardId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (boardRecommendationRepository.existsByBoardAndUser(board, user)) {
            throw new IllegalStateException("이미 추천한 게시글입니다.");
        }

        BoardRecommendation recommendation = BoardRecommendation.builder()
                .board(board)
                .user(user)
                .build();

        boardRecommendationRepository.save(recommendation);
        board.increaseRecommendCount();
    }

    @Transactional
    public void cancelRecommend(Long boardId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (!boardRecommendationRepository.existsByBoardAndUser(board, user)) {
            throw new IllegalStateException("추천하지 않은 게시글입니다.");
        }

        boardRecommendationRepository.deleteByBoardAndUser(board, user);
        board.decreaseRecommendCount();
    }
}
