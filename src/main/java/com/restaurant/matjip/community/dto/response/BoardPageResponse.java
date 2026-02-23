package com.restaurant.matjip.community.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record BoardPageResponse(

        /* ================== 게시글 데이터 ================== */

        List<BoardListResponse> notices,
        List<BoardListResponse> contents,

        /* ================== 페이징 정보 ================== */

        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last

) {

    public BoardPageResponse(
            List<BoardListResponse> notices,
            Page<BoardListResponse> page
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
