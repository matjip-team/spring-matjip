package com.restaurant.matjip.users.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import com.restaurant.matjip.users.constant.UserRole;
import com.restaurant.matjip.users.constant.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("사용자 아이디")
    private Long id;

    @Comment("이메일")
    @Column(nullable = false, unique = true)
    private String email;

    @Comment("사용자명")
    @Column(nullable = false)
    private String name;

    @Comment("닉네임")
    @Column(nullable = false)
    private String nickname;

    @Comment("패스워드")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Comment("사용자 역활")
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Comment("사용자 상태")
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @Comment("삭제일")
    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private UserProfile userProfile;

    // 연관관계 편의 메서드
    public void setUserProfile(UserProfile profile) {
        this.userProfile = profile;
        profile.setUser(this);
    }

    public void update(String name, String nickname) {
        this.name = name;
        this.nickname = nickname;
    }

    public void changeStatus(UserStatus status) {
        this.status = status;
    }

    public void withdraw() {
        this.status = UserStatus.DELETED;
    }

}
