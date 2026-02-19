package com.restaurant.matjip.blog.service;

import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.dto.request.BlogCreateRequest;
import com.restaurant.matjip.blog.dto.request.BlogUpdateRequest;
import com.restaurant.matjip.blog.repository.BlogRepository;
import com.restaurant.matjip.blog.repository.BlogViewRepository;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogCommandService {

    private final BlogRepository boardRepository;
    private final UserRepository userRepository;
    private final BlogViewRepository boardViewRepository;

    @Transactional
    public Long create(BlogCreateRequest request, Long userId) {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("user not found"));

        Blog board = Blog.builder()
                .title(request.getTitle())
                .content(resolvePrimaryContent(request.getContent(), request.getContentHtml(), null))
                .contentHtml(resolveHtmlContent(request.getContentHtml(), request.getContent(), null))
                .contentDelta(blankToNull(request.getContentDelta()))
                .boardType(request.getBoardType())
                .imageUrl(blankToNull(request.getImageUrl()))
                .user(writer)
                .viewCount(0)
                .recommendCount(0)
                .build();

        return boardRepository.save(board).getId();
    }

    @Transactional
    public void update(Long boardId, Long userId, BlogUpdateRequest req) {
        Blog board = boardRepository.findById(boardId)
                .orElseThrow();

        if (!board.getUser().getId().equals(userId)) {
            throw new RuntimeException("no permission");
        }

        board.setTitle(req.getTitle());
        board.setContent(resolvePrimaryContent(req.getContent(), req.getContentHtml(), board.getContent()));
        board.setContentHtml(resolveHtmlContent(req.getContentHtml(), req.getContent(), board.getContentHtml()));
        board.setContentDelta(blankToNull(req.getContentDelta()));
        board.setImageUrl(blankToNull(req.getImageUrl()));
        board.setBoardType(req.getBoardType());
    }

    @Transactional
    public void delete(Long boardId, Long userId) {
        Blog board = boardRepository.findById(boardId)
                .orElseThrow();

        if (!board.getUser().getId().equals(userId)) {
            throw new RuntimeException("no permission");
        }

        boardViewRepository.deleteByBoardId(boardId);
        boardRepository.delete(board);
    }

    private String resolvePrimaryContent(String content, String contentHtml, String fallback) {
        if (hasText(content)) {
            return content;
        }
        if (hasText(contentHtml)) {
            return contentHtml;
        }
        return fallback;
    }

    private String resolveHtmlContent(String contentHtml, String content, String fallback) {
        if (hasText(contentHtml)) {
            return contentHtml;
        }
        if (hasText(content)) {
            return content;
        }
        return fallback;
    }

    private String blankToNull(String value) {
        if (!hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
