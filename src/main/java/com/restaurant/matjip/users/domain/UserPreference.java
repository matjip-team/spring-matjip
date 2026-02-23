package com.restaurant.matjip.users.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import com.restaurant.matjip.data.domain.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "user_preferences", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","category_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreference extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("사용자선호 아이디")
    private Long id;

    @ManyToOne
    @Comment("사용자 아이디")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @Comment("카테고리 아이디")
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Comment("선호도 점수")
    @Column(name = "preference_score", nullable = false)
    private float preferenceScore;
}

