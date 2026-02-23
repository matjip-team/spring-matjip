package com.restaurant.matjip.community.repository;

import com.restaurant.matjip.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
select distinct c
from Comment c
left join fetch c.children
where c.board.id = :boardId
and c.parent is null
order by c.id asc
""")
    List<Comment> findRootComments(@Param("boardId") Long boardId);

    List<Comment> findByParentId(Long parentId);

    @Query("""
select distinct c
from Comment c
left join fetch c.children
where c.board.id = :boardId
and c.parent is null
order by c.id asc
""")
    List<Comment> findRootCommentsCreated(@Param("boardId") Long boardId);

    @Query("""
select distinct c
from Comment c
left join fetch c.children
where c.board.id = :boardId
and c.parent is null
order by c.id desc
""")
    List<Comment> findRootCommentsLatest(@Param("boardId") Long boardId);

    int countByBoardIdAndDeletedFalse(Long boardId);

    Optional<Comment> findByIdAndBoardId(Long id, Long boardId);
}
