package com.restaurant.matjip.data.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import com.restaurant.matjip.users.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "reviews") // 🔥 unique 제거
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("리뷰 아이디")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("사용자 아이디")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @Comment("레스토랑 아이디")
    private Restaurant restaurant;

    @Comment("평가")
    @Column(nullable = false)
    private int rating;

    @Comment("리뷰내용")
    @Column(columnDefinition = "TEXT")
    private String content;

    public Review(User user, Restaurant restaurant, int rating, String content) {
        this.user = user;
        this.restaurant = restaurant;
        this.rating = rating;
        this.content = content;
    }

    public void update(int rating, String content) {
        this.rating = rating;
        this.content = content;
    }
}
