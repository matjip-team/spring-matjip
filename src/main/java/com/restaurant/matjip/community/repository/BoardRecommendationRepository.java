package com.restaurant.matjip.community.repository;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.BoardRecommendation;
import com.restaurant.matjip.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRecommendationRepository extends JpaRepository<BoardRecommendation, Long> {

    boolean existsByBoardAndUser(Board board, User user);

    void deleteByBoardAndUser(Board board, User user);

    int countByBoard_Id(Long boardId);
}
