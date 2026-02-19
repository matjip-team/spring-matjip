package com.restaurant.matjip.blog.repository;

import com.restaurant.matjip.blog.domain.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {

    // 부모 댓글만 조회
    @Query("""
select distinct c
from BlogComment c
left join fetch c.children
where c.board.id = :boardId
and c.parent is null
order by c.id asc
""")
    List<BlogComment> findRootComments(@Param("boardId") Long boardId);

    // 특정 부모의 자식 댓글만 조회
    List<BlogComment> findByParentId(Long parentId);

    // 댓글목록 오래된순 정렬
    @Query("""
select distinct c
from BlogComment c
left join fetch c.children
where c.board.id = :boardId
and c.parent is null
order by c.id asc
""")
    List<BlogComment> findRootCommentsCreated(@Param("boardId") Long boardId);

    // 댓글목록 최신순 정렬
    @Query("""
select distinct c
from BlogComment c
left join fetch c.children
where c.board.id = :boardId
and c.parent is null
order by c.id desc
""")
    List<BlogComment> findRootCommentsLatest(@Param("boardId") Long boardId);

    int countByBoardIdAndDeletedFalse(Long boardId);

}


