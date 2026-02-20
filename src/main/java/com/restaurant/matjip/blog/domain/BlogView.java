package com.restaurant.matjip.blog.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "blog_views",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"blog_id", "user_id"})
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogView {

    /* ================== PK ================== */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ================== 게시글 ================== */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    /* ================== 사용자 ================== */

    @Column(name = "user_id", nullable = false)
    private Long userId;

    /* ================== 조회 시간 ================== */

    private LocalDateTime viewedAt;
}



