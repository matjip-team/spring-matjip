package com.restaurant.matjip.admin.blog.service;

import com.restaurant.matjip.blog.domain.Blog;
import com.restaurant.matjip.blog.domain.BlogComment;
import com.restaurant.matjip.blog.dto.request.BlogCommentCreateRequest;
import com.restaurant.matjip.blog.dto.response.BlogCommentResponse;
import com.restaurant.matjip.admin.blog.repository.AdminBlogCommentRepository;
import com.restaurant.matjip.admin.blog.repository.AdminBlogRepository;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminBlogCommentService {

    private final AdminBlogCommentRepository commentRepository;
    private final AdminBlogRepository blogRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(Long blogId, Long userId, BlogCommentCreateRequest req) {
        Blog blog = blogRepository.findById(blogId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        BlogComment parent = null;
        if (req.getParentId() != null) {
            parent = commentRepository.findById(req.getParentId()).orElseThrow();
            if (!parent.getBlog().getId().equals(blogId)) {
                throw new RuntimeException("부모 댓글이 해당 게시글의 댓글이 아닙니다.");
            }
        }

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

    @Transactional(readOnly = true)
    public List<BlogCommentResponse> getList(Long blogId, String sort) {
        List<BlogComment> roots;
        if ("created".equalsIgnoreCase(sort)) {
            roots = commentRepository.findRootCommentsCreated(blogId);
        } else {
            roots = commentRepository.findRootCommentsLatest(blogId);
        }
        return roots.stream().map(BlogCommentResponse::from).toList();
    }

    @Transactional
    public void update(Long commentId, Long adminUserId, String content) {
        BlogComment c = commentRepository.findById(commentId).orElseThrow();
        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("내용 없음");
        }
        c.updateContent(content);
    }

    @Transactional
    public void delete(Long commentId, Long adminUserId) {
        BlogComment c = commentRepository.findById(commentId).orElseThrow();
        c.softDelete();
    }
}
