package com.restaurant.matjip.community.repository;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 전체 게시글 최신순 조회
    List<Board> findAllByOrderByIdDesc();

    // 말머리별 게시글 최신순 조회 (boardType 기준)
    List<Board> findByBoardTypeOrderByIdDesc(BoardType boardType);
}