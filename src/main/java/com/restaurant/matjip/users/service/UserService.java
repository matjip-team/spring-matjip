package com.restaurant.matjip.users.service;

import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.dto.request.UserCreateRequest;
import com.restaurant.matjip.users.dto.response.UserResponse;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.restaurant.matjip.global.exception.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 생성
    public UserResponse create(UserCreateRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.valueOf(request.getRole()))
                .status(User.Status.valueOf(request.getStatus()))
                .build();
        return UserResponse.from(userRepository.save(user));
    }

    // 단일 조회
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return UserResponse.from(user);
    }

    // 전체 조회
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    // 수정
    public UserResponse update(Long id, UserCreateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        user = User.builder()
                .id(user.getId())
                .email(request.getEmail())
                //.name(request.getName())
                //.password(passwordEncoder.encode(request.getPassword()))
                //.role(user.getRole())
                .build();
        return UserResponse.from(userRepository.save(user));
    }

    // 삭제
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
