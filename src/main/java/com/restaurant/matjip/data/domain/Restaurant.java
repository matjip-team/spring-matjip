package com.restaurant.matjip.data.domain;

import com.restaurant.matjip.common.domain.BaseEntity;
import com.restaurant.matjip.users.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Restaurant id")
    private Long id;

    @Comment("Restaurant name")
    @Column(length = 100, nullable = false)
    private String name;

    @Comment("Address")
    @Column(length = 255, nullable = false)
    private String address;

    @Comment("Latitude")
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Comment("Longitude")
    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Comment("Phone")
    @Column(length = 20)
    private String phone;

    @Comment("Description")
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(unique = true)
    @Comment("External id")
    private String externalId;

    @Comment("Data source")
    private String source;

    @Comment("Restaurant image URL")
    @Column(length = 500)
    private String imageUrl;

    @Comment("Business license file URL")
    @Column(length = 500)
    private String businessLicenseFileUrl;

    @Comment("Rejected reason")
    @Column(columnDefinition = "TEXT")
    private String rejectedReason;

    @Enumerated(EnumType.STRING)
    @Comment("Approval status")
    @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'APPROVED'")
    @Builder.Default
    private RestaurantApprovalStatus approvalStatus = RestaurantApprovalStatus.APPROVED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registered_by")
    private User registeredBy;

    @ManyToMany
    @JoinTable(
            name = "restaurant_categories",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Builder.Default
    private Set<Category> categories = new HashSet<>();

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
        r.approvalStatus = RestaurantApprovalStatus.APPROVED;
        return r;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }
}
