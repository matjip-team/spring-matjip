package com.restaurant.matjip.blog.service;

import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.domain.BlogRecommendation;
import com.restaurant.matjip.blog.repository.BlogRepository;
import com.restaurant.matjip.blog.repository.BlogRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogRecommendationService {

    private final BlogRepository boardRepository;
    private final BlogRecommendationRepository boardRecommendationRepository;

    @Transactional
    public void toggleRecommend(Long boardId, Long userId) {

        Blog board = boardRepository.findById(boardId)
                .orElseThrow();

        // 이미 추천했으면 취소
        if (boardRecommendationRepository.existsByBoardIdAndUserId(boardId, userId)) {

            boardRecommendationRepository.deleteByBoardIdAndUserId(boardId, userId);
            board.decreaseRecommendCount();
            return;
        }

        // 추천
        BlogRecommendation rec = BlogRecommendation.builder()
                .board(board)
                .userId(userId)
                .build();

        boardRecommendationRepository.save(rec);
        board.increaseRecommendCount();
    }
}


