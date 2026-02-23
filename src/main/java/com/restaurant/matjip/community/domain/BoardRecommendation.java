package com.restaurant.matjip.community.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(
        name = "board_recommendations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"board_id", "user_id"})
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRecommendation  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
