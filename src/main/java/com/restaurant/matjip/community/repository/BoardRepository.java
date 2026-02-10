package com.restaurant.matjip.community.repository;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("""
        SELECT b FROM Board b
        WHERE (:type IS NULL OR b.boardType = :type)
        ORDER BY
          CASE WHEN b.boardType = :notice THEN 0 ELSE 1 END,
          b.id DESC
    """)
    List<Board> findAllPinnedNoticeFirst(
            @Param("type") BoardType type,
            @Param("notice") BoardType notice
    );

    // 전체 게시글 최신순 조회
    List<Board> findAllByOrderByIdDesc();

    // 말머리별 게시글 최신순 조회 (boardType 기준)
    List<Board> findByBoardTypeOrderByIdDesc(BoardType boardType);
}