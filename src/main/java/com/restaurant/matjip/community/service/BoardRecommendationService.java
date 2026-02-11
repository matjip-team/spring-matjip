package com.restaurant.matjip.community.service;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.BoardRecommendation;
import com.restaurant.matjip.community.repository.BoardRepository;
import com.restaurant.matjip.community.repository.BoardRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardRecommendationService {

    private final BoardRepository boardRepository;
    private final BoardRecommendationRepository boardRecommendationRepository;

    @Transactional
    public void toggleRecommend(Long boardId, Long userId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow();

        // 이미 추천했으면 취소
        if (boardRecommendationRepository.existsByBoardIdAndUserId(boardId, userId)) {

            boardRecommendationRepository.deleteByBoardIdAndUserId(boardId, userId);
            board.decreaseRecommendCount();
            return;
        }

        // 추천
        BoardRecommendation rec = BoardRecommendation.builder()
                .board(board)
                .userId(userId)
                .build();

        boardRecommendationRepository.save(rec);
        board.increaseRecommendCount();
    }
}
