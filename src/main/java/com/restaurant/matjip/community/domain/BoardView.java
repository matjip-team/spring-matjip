package com.restaurant.matjip.community.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "board_views",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"board_id", "user_id"})
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardView {

    /* ================== PK ================== */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ================== 게시글 ================== */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    /* ================== 사용자 ================== */

    @Column(name = "user_id", nullable = false)
    private Long userId;

    /* ================== 조회 시간 ================== */

    private LocalDateTime viewedAt;
}
