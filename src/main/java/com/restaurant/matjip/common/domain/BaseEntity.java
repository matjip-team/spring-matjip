package com.restaurant.matjip.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {

    @CreatedBy
    @Comment("작성자")
    @Column(updatable = false)
    private Long createdBy;

    @CreatedDate
    @Comment("작성일")
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Comment("수정자")
    private Long updatedBy;

    @LastModifiedDate
    @Comment("수정일")
    private LocalDateTime updatedAt;
}
