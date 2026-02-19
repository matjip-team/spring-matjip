package com.restaurant.matjip.blog.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import com.restaurant.matjip.users.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog_comments")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 댓글 내용 */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /* 어떤 게시글의 댓글인지 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog board;

    /* 작성자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /* 부모 댓글 (null이면 일반 댓글, 있으면 대댓글) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BlogComment parent;

    @Column(nullable = false)
    private boolean deleted = false;

    /* 대댓글 목록 */
    @OneToMany(mappedBy = "parent")
    private List<BlogComment> children = new ArrayList<>();

    /* ===============================
       ✅ 댓글 수정용 메서드
    ================================ */
    public void updateContent(String content) {
        this.content = content;
    }

    public void softDelete() {
        this.deleted = true;
        this.content = "삭제된 댓글입니다.";
    }
}




