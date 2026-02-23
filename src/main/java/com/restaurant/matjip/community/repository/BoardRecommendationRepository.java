package com.restaurant.matjip.community.repository;

import com.restaurant.matjip.community.domain.BoardRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRecommendationRepository
        extends JpaRepository<BoardRecommendation, Long> {

    /* 추천 여부 */
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    /* 추천 취소 */
    void deleteByBoardIdAndUserId(Long boardId, Long userId);

    /* 추천 수 */
    int countByBoardId(Long boardId);
}
