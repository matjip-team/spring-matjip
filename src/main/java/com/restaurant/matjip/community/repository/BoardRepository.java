package com.restaurant.matjip.community.repository;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByBoardTypeOrderByIdDesc(BoardType boardType);

    List<Board> findByBoardTypeAndHiddenFalseOrderByIdDesc(BoardType boardType);

    @Query("""
        SELECT b FROM Board b
        WHERE
            (:type IS NULL OR b.boardType = :type)
        AND (
            :keyword IS NULL OR
            b.title LIKE %:keyword% OR
            b.content LIKE %:keyword%
        )
        AND b.boardType <> 'NOTICE'
        ORDER BY b.id DESC
    """)
    Page<Board> searchNormalBoards(
            @Param("type") BoardType type,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
select b from Board b
where b.boardType <> 'NOTICE'
and (:type is null or b.boardType = :type)
and lower(b.title) like lower(concat('%', :keyword, '%'))
order by b.id desc
""")
    Page<Board> searchTitle(
            BoardType type,
            String keyword,
            Pageable pageable
    );

    @Query("""
select b from Board b
where b.boardType <> 'NOTICE'
and (:type is null or b.boardType = :type)
and lower(b.content) like lower(concat('%', :keyword, '%'))
order by b.id desc
""")
    Page<Board> searchContent(
            BoardType type,
            String keyword,
            Pageable pageable
    );

    @Query("""
select b from Board b
join b.user u
where b.boardType <> 'NOTICE'
and (:type is null or b.boardType = :type)
and lower(u.nickname) like lower(concat('%', :keyword, '%'))
order by b.id desc
""")
    Page<Board> searchAuthor(
            BoardType type,
            String keyword,
            Pageable pageable
    );

    @Query("""
select distinct b from Board b
join Comment c on c.board = b
where b.boardType <> 'NOTICE'
and c.deleted = false
and (:type is null or b.boardType = :type)
and lower(c.content) like lower(concat('%', :keyword, '%'))
order by b.id desc
""")
    Page<Board> searchComment(
            BoardType type,
            String keyword,
            Pageable pageable
    );
}
