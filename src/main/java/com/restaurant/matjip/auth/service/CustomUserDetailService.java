package com.restaurant.matjip.auth.service;

import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import com.restaurant.matjip.users.domain.User;
import com.restaurant.matjip.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.USER_NOT_FOUND)
                );

        String authority = "ROLE_" + user.getRole().name();

        return CustomUserDetails.builder()
                .id(user.getId())
                .username(user.getEmail())
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .password(user.getPasswordHash())
                .profileImageUrl(user.getProfile().getProfileImageUrl())
                .authorities(List.of(new SimpleGrantedAuthority(authority)))
                .build();
    }
}
