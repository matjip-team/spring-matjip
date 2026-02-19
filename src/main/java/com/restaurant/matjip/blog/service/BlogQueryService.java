package com.restaurant.matjip.blog.service;

import com.restaurant.matjip.blog.controller.enums.BlogSearchType;
import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.domain.BlogType;
import com.restaurant.matjip.blog.domain.BlogView;
import com.restaurant.matjip.blog.dto.response.BlogDetailResponse;
import com.restaurant.matjip.blog.dto.response.BlogListResponse;
import com.restaurant.matjip.blog.dto.response.BlogPageResponse;
import com.restaurant.matjip.blog.repository.BlogRecommendationRepository;
import com.restaurant.matjip.blog.repository.BlogRepository;
import com.restaurant.matjip.blog.repository.BlogViewRepository;
import com.restaurant.matjip.blog.repository.BlogCommentRepository;
import com.restaurant.matjip.global.common.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogQueryService {

    private final BlogRepository boardRepository;
    private final BlogViewRepository boardViewRepository;
    private final BlogRecommendationRepository boardRecommendationRepository;
    private final BlogCommentRepository commentRepository;

    /* ================== 게시글 목록 조회 ================== */

    @Transactional(readOnly = true)
    public BlogPageResponse getBoards(
            BlogType type,
            String keyword,
            BlogSearchType searchType,
            Pageable pageable
    ) {

        List<BlogListResponse> notices =
                boardRepository.findByBoardTypeOrderByIdDesc(BlogType.NOTICE)
                        .stream()
                        .map(board -> {
                            int count = commentRepository.countByBoardIdAndDeletedFalse(board.getId());
                            return BlogListResponse.from(board, count);
                        })
                        .toList();

        Page<Blog> page;

        switch (searchType) {
            case TITLE ->
                    page = boardRepository.searchTitle(type, keyword, pageable);

            case CONTENT ->
                    page = boardRepository.searchContent(type, keyword, pageable);

            case AUTHOR ->
                    page = boardRepository.searchAuthor(type, keyword, pageable);

            case COMMENT ->
                    page = boardRepository.searchComment(type, keyword, pageable);

            default ->
                    page = boardRepository.searchNormalBoards(type, keyword, pageable);
        }

        Page<BlogListResponse> normalPage =
                page.map(board -> {
                    int count = commentRepository.countByBoardIdAndDeletedFalse(board.getId());
                    return BlogListResponse.from(board, count);
                });
        return new BlogPageResponse(notices, normalPage);
    }


    /* ================== 게시글 상세 조회 + 조회수 처리 ================== */

    @Transactional
    public BlogDetailResponse getDetail(Long boardId, CustomUserDetails userDetails) {

        // 게시글 조회
        Blog board = boardRepository.findById(boardId)
                .orElseThrow();

        // 🔹 비로그인 유저 → 그냥 조회수 증가
        if (userDetails == null) {
            board.increaseViewCount();
            int commentCount = commentRepository.countByBoardIdAndDeletedFalse(boardId);
            return new BlogDetailResponse(board, false, commentCount);
        }

        Long userId = userDetails.getId();

        // 🔹 로그인 유저 + 처음 보는 글일 때만 조회수 증가
        if (!boardViewRepository.existsByBoardIdAndUserId(boardId, userId)) {

            BlogView view = BlogView.builder()
                    .board(board)
                    .userId(userId)
                    .viewedAt(LocalDateTime.now())
                    .build();

            boardViewRepository.save(view);
            board.increaseViewCount();
        }

        boolean recommended =
                boardRecommendationRepository.existsByBoardIdAndUserId(boardId, userId);

        int commentCount = commentRepository.countByBoardIdAndDeletedFalse(boardId);
        return new BlogDetailResponse(board, recommended, commentCount);
    }
}



