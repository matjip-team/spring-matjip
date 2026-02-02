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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type", nullable = false)
    private BoardType boardType;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "recommend_count", nullable = false)
    private int recommendCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /* ================== 연관관계 ================== */

    // Comment는 지금 파일에 없으니 일단 유지/없으면 제거해도 됨
    // @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Comment> comments = new ArrayList<>();

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
        if (this.recommendCount > 0) this.recommendCount--;
    }
}
