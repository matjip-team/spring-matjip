package com.restaurant.matjip.blog.repository;

import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.domain.BlogType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    /* ================== 공지 목록 (상단 고정용) ================== */

    List<Blog> findByBoardTypeOrderByIdDesc(BlogType boardType);


    /* ================== 일반 게시글 검색 + 필터 + 페이징 ================== */

    @Query("""
        SELECT b FROM Blog b
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
    Page<Blog> searchNormalBoards(
            @Param("type") BlogType type,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /* ================== 제목 검색 ================== */

    @Query("""
select b from Blog b
where b.boardType <> 'NOTICE'
and (:type is null or b.boardType = :type)
and lower(b.title) like lower(concat('%', :keyword, '%'))
order by b.id desc
""")
    Page<Blog> searchTitle(
            BlogType type,
            String keyword,
            Pageable pageable
    );


    /* ================== 내용 검색 ================== */

    @Query("""
select b from Blog b
where b.boardType <> 'NOTICE'
and (:type is null or b.boardType = :type)
and lower(b.content) like lower(concat('%', :keyword, '%'))
order by b.id desc
""")
    Page<Blog> searchContent(
            BlogType type,
            String keyword,
            Pageable pageable
    );


    /* ================== 글쓴이 검색 ================== */

    @Query("""
select b from Blog b
join b.user u
where b.boardType <> 'NOTICE'
and (:type is null or b.boardType = :type)
and lower(u.nickname) like lower(concat('%', :keyword, '%'))
order by b.id desc
""")
    Page<Blog> searchAuthor(
            BlogType type,
            String keyword,
            Pageable pageable
    );


    /* ================== 댓글 검색 ================== */

    @Query("""
select distinct b from Blog b
join BlogComment c on c.board = b
where b.boardType <> 'NOTICE'
and c.deleted = false
and (:type is null or b.boardType = :type)
and lower(c.content) like lower(concat('%', :keyword, '%'))
order by b.id desc
""")
    Page<Blog> searchComment(
            BlogType type,
            String keyword,
            Pageable pageable
    );
}

