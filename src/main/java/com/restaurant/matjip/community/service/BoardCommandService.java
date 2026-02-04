package com.restaurant.matjip.community.service;

import com.restaurant.matjip.community.domain.Board;
import com.restaurant.matjip.community.dto.request.BoardCreateRequest;
import com.restaurant.matjip.community.repository.BoardRepository;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardCommandService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long create(BoardCreateRequest request, Long userId) {

        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자 없음"));

        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .boardType(request.getBoardType())
                .imageUrl(request.getImageUrl())
                .user(writer)
                .viewCount(0)
                .recommendCount(0)
                .build();

        return boardRepository.save(board).getId();
    }
}
