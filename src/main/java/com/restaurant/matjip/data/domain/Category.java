package com.restaurant.matjip.data.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("카테고리 아이디")
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    @Comment("카테고리명")
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Restaurant> restaurants = new HashSet<>();
}

