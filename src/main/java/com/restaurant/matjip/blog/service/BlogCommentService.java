package com.restaurant.matjip.blog.service;

import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.domain.BlogComment;
import com.restaurant.matjip.blog.dto.request.BlogCommentCreateRequest;
import com.restaurant.matjip.blog.dto.response.BlogCommentResponse;
import com.restaurant.matjip.blog.repository.BlogRepository;
import com.restaurant.matjip.blog.repository.BlogCommentRepository;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogCommentService {

    private final BlogCommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    /* 댓글 + 대댓글 작성 */
    @Transactional
    public void create(Long blogId, Long userId, BlogCommentCreateRequest req) {

        Blog blog = blogRepository.findById(blogId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        BlogComment parent = null;

        // ✅ 대댓글이면 부모 조회
        if (req.getParentId() != null) {
            parent = commentRepository.findById(req.getParentId()).orElseThrow();

            // ✅ 부모가 같은 게시글 소속인지 검증 (트리 꼬임 방지)
            if (!parent.getBlog().getId().equals(blogId)) {
                throw new RuntimeException("부모 댓글이 해당 게시글의 댓글이 아닙니다.");
            }
        }

        // ✅ 여기서 content는 반드시 req.getContent()
        BlogComment comment = BlogComment.builder()
                .blog(blog)
                .user(user)
                .content(req.getContent())
                .parent(parent)
                .build();

        if (parent != null) {
            parent.getChildren().add(comment);
        }

        commentRepository.save(comment);
    }

    /* 댓글 목록 + 정렬 기능*/
    @Transactional(readOnly = true)
    public List<BlogCommentResponse> getList(Long blogId, String sort) {

        List<BlogComment> roots;

        if ("created".equalsIgnoreCase(sort)) {
            roots = commentRepository.findRootCommentsCreated(blogId); // 오래된순
        } else {
            roots = commentRepository.findRootCommentsLatest(blogId);  // 최신순(기본)
        }

        return roots.stream()
                .map(BlogCommentResponse::from)
                .toList();
    }

    /* 댓글 수정 */
    @Transactional
    public void update(Long commentId, Long userId, String content) {
        BlogComment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED_ERROR));

//        if (!c.getUser().getId().equals(userId)) {
//            new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
//        }

        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("내용 없음");
        }

        c.updateContent(content);
    }

    /* 댓글 삭제 */
    @Transactional
    public void delete(Long commentId, Long userId) {

        BlogComment c = commentRepository.findById(commentId).orElseThrow();

        if (!c.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한 없음");
        }

        c.softDelete();
    }
}


