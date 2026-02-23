package com.restaurant.matjip.blog.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(
        name = "blog_recommendations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"blog_id", "user_id"})
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogRecommendation  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}



