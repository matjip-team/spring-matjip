package com.restaurant.matjip.admin.blog.repository;

import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.domain.BlogType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 관리자 블로그용 Repository - JpaRepository 직접 상속, 동일 엔티티(blogs) 사용
 */
@Repository
public interface AdminBlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findByBlogTypeOrderByIdDesc(BlogType blogType);

    @Query("""
        SELECT b FROM Blog b
        WHERE
            (:type IS NULL OR b.blogType = :type)
        AND (
            :keyword IS NULL OR
            b.title LIKE %:keyword% OR
            b.content LIKE %:keyword%
        )
        AND b.blogType <> 'NOTICE'
        ORDER BY b.id DESC
    """)
    Page<Blog> searchNormalBlogs(
            @Param("type") BlogType type,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
select b from Blog b
where b.blogType <> 'NOTICE'
and (:type is null or b.blogType = :type)
and lower(b.title) like lower(concat('%', :keyword, '%'))
order by b.id desc
""")
    Page<Blog> searchTitle(
            BlogType type,
            String keyword,
            Pageable pageable
    );

    @Query("""
select b from Blog b
where b.blogType <> 'NOTICE'
and (:type is null or b.blogType = :type)
and lower(b.content) like lower(concat('%', :keyword, '%'))
order by b.id desc
""")
    Page<Blog> searchContent(
            BlogType type,
            String keyword,
            Pageable pageable
    );

    @Query("""
select b from Blog b
join b.user u
where b.blogType <> 'NOTICE'
and (:type is null or b.blogType = :type)
and lower(u.nickname) like lower(concat('%', :keyword, '%'))
order by b.id desc
""")
    Page<Blog> searchAuthor(
            BlogType type,
            String keyword,
            Pageable pageable
    );

    @Query("""
select distinct b from Blog b
join BlogComment c on c.blog = b
where b.blogType <> 'NOTICE'
and c.deleted = false
and (:type is null or b.blogType = :type)
and lower(c.content) like lower(concat('%', :keyword, '%'))
order by b.id desc
""")
    Page<Blog> searchComment(
            BlogType type,
            String keyword,
            Pageable pageable
    );
}
