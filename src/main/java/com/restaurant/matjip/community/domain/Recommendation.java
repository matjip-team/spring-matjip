package com.restaurant.matjip.community.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import com.restaurant.matjip.users.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "recommendations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"board_id", "user_id"})
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommendation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ================== 추천 대상 게시글 ================== */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    /* ================== 추천한 유저 ================== */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
// 임시
    private Double score;     // 🔥 이거 추가

    @Column(columnDefinition = "TEXT")
    private String reason;    // 🔥 이거 추가
}
