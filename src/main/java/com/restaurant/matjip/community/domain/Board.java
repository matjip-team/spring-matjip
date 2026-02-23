package com.restaurant.matjip.community.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import com.restaurant.matjip.users.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseEntity {

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
    private BoardType boardType;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "recommend_count", nullable = false)
    private int recommendCount;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "hidden", nullable = false)
    private boolean hidden = false;

    @Column(name = "report_count", nullable = false)
    private int reportCount = 0;

    /* ================== 연관 관계 ================== */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

     //댓글
     @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardRecommendation> recommendations = new ArrayList<>();

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

    public void hide() {
        this.hidden = true;
    }

    public void restore() {
        this.hidden = false;
    }

    public void increaseReportCount() {
        this.reportCount++;
    }

    public void decreaseReportCount() {
        if (this.reportCount > 0) {
            this.reportCount--;
        }
    }
}
