package com.restaurant.matjip.data.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import com.restaurant.matjip.users.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(
        name = "restaurant_likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "restaurant_id"})
        }
)
@Getter
@NoArgsConstructor
public class RestaurantLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("좋아요 아이디")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("사용자 아이디")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @Comment("레스토랑 아이디")
    private Restaurant restaurant;

    public RestaurantLike(User user, Restaurant restaurant) {
        this.user = user;
        this.restaurant = restaurant;
    }
}
