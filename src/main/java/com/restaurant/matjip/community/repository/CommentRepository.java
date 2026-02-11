package com.restaurant.matjip.community.repository;

import com.restaurant.matjip.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 부모 댓글만 조회
    @Query("""
select distinct c
from Comment c
left join fetch c.children
where c.board.id = :boardId
and c.parent is null
order by c.id asc
""")
    List<Comment> findRootComments(@Param("boardId") Long boardId);

    // 특정 부모의 자식 댓글만 조회
    List<Comment> findByParentId(Long parentId);

    // 댓글목록 오래된순 정렬
    @Query("""
select distinct c
from Comment c
left join fetch c.children
where c.board.id = :boardId
and c.parent is null
order by c.id asc
""")
    List<Comment> findRootCommentsCreated(@Param("boardId") Long boardId);

    // 댓글목록 최신순 정렬
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

}
