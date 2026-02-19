package com.restaurant.matjip.blog.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import com.restaurant.matjip.users.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog extends BaseEntity {

    /* ================== 기본 컬럼 ================== */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "content_html", columnDefinition = "TEXT")
    private String contentHtml;

    @Column(name = "content_delta", columnDefinition = "TEXT")
    private String contentDelta;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type", nullable = false)
    private BlogType boardType;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "recommend_count", nullable = false)
    private int recommendCount;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /* ================== 연관 관계 ================== */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

     //댓글
     @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<BlogComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogRecommendation> recommendations = new ArrayList<>();

    /* ================== 비즈니스 메서드 ================== */

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseRecommendCount() {
        this.recommendCount++;
    }

    public void decreaseRecommendCount() {
        if (this.recommendCount > 0) {
            this.recommendCount--;
        }
    }
}



