package com.restaurant.matjip.global.component;

import com.restaurant.matjip.auth.service.CustomUserDetailService;
import com.restaurant.matjip.global.common.CustomUserDetails;
import com.restaurant.matjip.users.constant.UserStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter 클래스
 * - 요청마다 JWT를 확인하고 인증 정보를 SecurityContext에 세팅하는 역할
 * - OncePerRequestFilter → 요청당 한 번만 실행되는 필터
 * - JWT 검증 같은 인증/인가 필터는 한 번만 실행하는 것이 효율적
 *
 * 작성자: Shawn Lee
 * 작성일: 2026-01-29
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //JWT 생성/검증/추출 기능 제공
    private final JwtUtil jwtUtil;
    //Spring Security UserDetailsService 구현체
    private final CustomUserDetailService customUserDetailService;

    @Value("${jwt.access-token-name}")
    private String accessTokenName;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        //쿠키에서 접근 토큰 가져오기
        String token = getTokenFromCookie(request);

        //토큰 검증 → 유효하면 username 추출
        if (token != null && jwtUtil.validateToken(token)) {
            //JWT에서 사용자 이름 추출 (sub 클레임)
            String username = jwtUtil.extractUsername(token);

            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailService.loadUserByUsername(username);

            // 🔥 탈퇴 회원 차단
            if (UserStatus.DELETED.equals(userDetails.getUserStatus())) {
                filterChain.doFilter(request, response);
                return;
            }

            if (UserStatus.BLOCKED.equals(userDetails.getUserStatus())) {
                filterChain.doFilter(request, response);
                return;
            }
            /*
              Spring Security 인증 객체
              첫 번째 파라미터: 사용자 정보
              두 번째: 인증에 사용되는 패스워드 (여기선 null, 이미 JWT로 인증했으므로)
              세 번째: 권한 정보
            */
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            //현재 요청의 인증 컨텍스트에 저장, 이후 컨트롤러에서 @AuthenticationPrincipal로 접근 가능
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        //필터 체인을 멈추지 않고 다음 필터/컨트롤러로 요청 전달
        filterChain.doFilter(request, response);
    }

    /*
        JWT를 HTTP 요청 쿠키에서 읽도록 설계됨
    */
    private String getTokenFromCookie(HttpServletRequest request) {/*
          요청에 쿠키가 존재하면 순회하며 이름과 일치하는 쿠키 값을 반환
          없으면 null → JWT가 없다는 의미
        */
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if (accessTokenName.equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }
}
