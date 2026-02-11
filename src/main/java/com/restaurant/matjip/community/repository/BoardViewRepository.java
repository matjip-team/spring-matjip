package com.restaurant.matjip.community.repository;

import com.restaurant.matjip.community.domain.BoardView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardViewRepository
        extends JpaRepository<BoardView, Long> {

    /* ================== 조회 여부 확인 ================== */

    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    void deleteByBoardId(Long boardId);
}