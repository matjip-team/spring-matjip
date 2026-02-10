package com.restaurant.matjip.users.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile extends BaseEntity {

    @Id
    @Comment("사용자 아이디")
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Comment("닉네임(벌칭)")
    @Column(length = 50, nullable = false)
    private String nickname;

    @Comment("프로파일 이미지")
    @Column(length = 500)
    private String profileImageUrl;

    @Comment("자기소개")
    @Column(columnDefinition = "TEXT")
    private String bio;
}

