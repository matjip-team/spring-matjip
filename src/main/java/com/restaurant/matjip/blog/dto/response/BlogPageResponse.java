package com.restaurant.matjip.blog.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record BlogPageResponse(

        /* ================== 게시글 데이터 ================== */

        List<BlogListResponse> notices,
        List<BlogListResponse> contents,

        /* ================== 페이징 정보 ================== */

        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last

) {

    public BlogPageResponse(
            List<BlogListResponse> notices,
            Page<BlogListResponse> page
    ) {

        this(
                notices,
                page.getContent(),        // 실제 게시글 목록
                page.getNumber(),         // 현재 페이지
                page.getSize(),           // 페이지 사이즈
                page.getTotalElements(),  // 전체 게시글 수
                page.getTotalPages(),     // 전체 페이지 수
                page.isLast()             // 마지막 페이지 여부
        );
    }
}


