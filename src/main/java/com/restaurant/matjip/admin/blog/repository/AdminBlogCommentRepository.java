package com.restaurant.matjip.admin.blog.repository;

import com.restaurant.matjip.blog.domain.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminBlogCommentRepository extends JpaRepository<BlogComment, Long> {

    @Query("""
select distinct c
from BlogComment c
left join fetch c.children
where c.blog.id = :blogId
and c.parent is null
order by c.id asc
""")
    List<BlogComment> findRootComments(@Param("blogId") Long blogId);

    List<BlogComment> findByParentId(Long parentId);

    @Query("""
select distinct c
from BlogComment c
left join fetch c.children
where c.blog.id = :blogId
and c.parent is null
order by c.id asc
""")
    List<BlogComment> findRootCommentsCreated(@Param("blogId") Long blogId);

    @Query("""
select distinct c
from BlogComment c
left join fetch c.children
where c.blog.id = :blogId
and c.parent is null
order by c.id desc
""")
    List<BlogComment> findRootCommentsLatest(@Param("blogId") Long blogId);

    int countByBlogIdAndDeletedFalse(Long blogId);
}
