package com.restaurant.matjip.community.service;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.domain.Comment;
import com.restaurant.matjip.community.dto.request.CommentCreateRequest;
import com.restaurant.matjip.community.dto.response.CommentResponse;
import com.restaurant.matjip.community.repository.BoardRepository;
import com.restaurant.matjip.community.repository.CommentRepository;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    /* 댓글 + 대댓글 작성 */
    @Transactional
    public void create(Long boardId, Long userId, CommentCreateRequest req) {

        Board board = boardRepository.findById(boardId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Comment parent = null;

        // ✅ 대댓글이면 부모 조회
        if (req.getParentId() != null) {
            parent = commentRepository.findById(req.getParentId()).orElseThrow();

            // ✅ 부모가 같은 게시글 소속인지 검증 (트리 꼬임 방지)
            if (!parent.getBoard().getId().equals(boardId)) {
                throw new RuntimeException("부모 댓글이 해당 게시글의 댓글이 아닙니다.");
            }
        }

        // ✅ 여기서 content는 반드시 req.getContent()
        Comment comment = Comment.builder()
                .board(board)
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
    public List<CommentResponse> getList(Long boardId, String sort) {

        List<Comment> roots;

        if ("created".equalsIgnoreCase(sort)) {
            roots = commentRepository.findRootCommentsCreated(boardId); // 오래된순
        } else {
            roots = commentRepository.findRootCommentsLatest(boardId);  // 최신순(기본)
        }

        return roots.stream()
                .map(CommentResponse::from)
                .toList();
    }

    /* 댓글 수정 */
    @Transactional
    public void update(Long commentId, Long userId, String content) {
        Comment c = commentRepository.findById(commentId).orElseThrow();

        if (!c.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한 없음");
        }

        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("내용 없음");
        }

        c.updateContent(content);
    }

    /* 댓글 삭제 */
    @Transactional
    public void delete(Long commentId, Long userId) {

        Comment c = commentRepository.findById(commentId).orElseThrow();

        if (!c.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한 없음");
        }

        c.softDelete();
    }
}
