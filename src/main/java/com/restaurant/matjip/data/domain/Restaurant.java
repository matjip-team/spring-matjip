package com.restaurant.matjip.data.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurants")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String address;

    /* DB 구조 유지 */
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(length = 20)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(unique = true)
    private String externalId;

    private String source;

    /* 이미지 추가 */
    @Column(length = 500)
    private String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "restaurant_categories",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    /* 기존 로직 유지 */
    public static Restaurant fromPython(
            String externalId,
            String name,
            String address,
            double lat,
            double lng,
            String source
    ) {
        Restaurant r = new Restaurant();
        r.externalId = externalId;
        r.name = name;
        r.address = address;
        r.latitude = BigDecimal.valueOf(lat);
        r.longitude = BigDecimal.valueOf(lng);
        r.source = source;
        return r;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }
}
