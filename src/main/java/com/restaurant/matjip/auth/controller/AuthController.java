package com.restaurant.matjip.auth.controller;

import com.restaurant.matjip.auth.dto.request.LoginRequest;
import com.restaurant.matjip.auth.dto.response.LoginResponse;
import com.restaurant.matjip.global.common.ApiResponse;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.global.component.JwtUtil;
import com.restaurant.matjip.global.exception.BusinessException;
import com.restaurant.matjip.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager; //로그인 시 인증 처리
    private final JwtUtil jwtUtil; //JWT 생성, 검증, 추출

    @PostMapping
    public ApiResponse<LoginResponse> login(
            @RequestBody @Valid LoginRequest request, HttpServletResponse response) {

        //UserDetailsService.loadUserByUsername() 호출함.
        try {
            // 이곳에서 PasswordEncoder.matches(rawPassword, encodedPassword) 자동 호출
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

            String accessToken = jwtUtil.generateAccessToken(user.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            // 권한 문자열 리스트로 변환
            List<String> roles = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            jwtUtil.addTokensToCookie(response, accessToken, refreshToken);

            return ApiResponse.success(
                    LoginResponse.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .name(user.getName())
                            .nickname(user.getNickname())
                            .roles(roles)
                            .build()
            );

        } catch (AuthenticationException e) { // BadCredentials, Disabled 등 모두 상속
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

    }

    //AccessToken 재발급 API
    @PostMapping("/refresh")
    public ApiResponse<Void> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {

        //RefreshToken이 유효하면 username 추출 → 새로운 AccessToken 발급
        if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            String newAccessToken = jwtUtil.generateAccessToken(username);

            jwtUtil.addNewTokensToCookie(response, newAccessToken);

            return ApiResponse.success(null);
        }
        throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
    }

    //JWT 쿠키 삭제
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {

        jwtUtil.deleteCookie(response);

        return ApiResponse.success(null);
    }

}
