package com.restaurant.matjip.global.component;

//JwtUtil 클래스는 **JWT(Json Web Token)**을 생성하고 검증하는 핵심 유틸리티

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JwtUtil 클래스
 * - JwtUtil 클래스는 **JWT(Json Web Token)**을 생성하고 검증하는 핵심 유틸리티
 * - OncePerRequestFilter → 요청당 한 번만 실행되는 필터
 * - JWT 검증 같은 인증/인가 필터는 한 번만 실행하는 것이 효율적
 * 작성자: Shawn Lee
 * 작성일: 2026-01-29
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey; //JWT 서명용 비밀키

    private Key key; //HMAC-SHA 키로 변환 후 사용.

//    private final long ACCESS_TOKEN_EXPIRATION;
//    private final long REFRESH_TOKEN_EXPIRATION;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Value("${cookie.access-token-max-age}")
    private int accessCookieMaxAge;

    @Value("${cookie.refresh-token-max-age}")
    private int refreshCookieMaxAge;

    @Value("${jwt.access-token-name}")
    private String accessTokenName;

    @Value("${jwt.refresh-token-name}")
    private String refreshTokenName;

    public JwtUtil() {
//        ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 15; // 엑세스 토큰 만료 시간 15분
//        REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 리프레시 토큰 만료 시간 7일
    }

    //매번 토큰 생성/검증 시 secretKey.getBytes()를 직접 쓰지 않고, 한 번 만들어 재사용
    @PostConstruct
    private void init() {
        // HS256(HMAC-SHA256) - 서명(Signature)을 만들고 검증하는 비밀 문자열
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    //토큰 생성 메서드(AccessToken)
    public String generateAccessToken(String username) {
        return createToken(username, accessTokenExpiration);
    }

    //토큰 생성 메서드(RefreshToken)
    public String generateRefreshToken(String username) {
        return createToken(username, refreshTokenExpiration);
    }

    private String createToken(String username, long expireMillis) {
        /*페이로드(Payload) HEADER.PAYLOAD.SIGNATURE
          HEADER 는 jjwt가 자동으로 만들어줌
          Jwts.builder()
           ├─ Header     ← 자동 생성
           ├─ Payload    ← setSubject / setIssuedAt / setExpiration
           ├─ Signature  ← signWith(key, HS256)
           └─ compact()  ← 최종 문자열 생성
          페이로드는 Claims(클레임) 라고 불리는 데이터 집합
          등록된 클레임(Registered Claims): JWT 표준에서 정의한 예약 키
          iss → 발급자(Issuer)
          sub → 주체(Subject)
          aud → 대상자(Audience)
          exp → 만료시간(Expiration Time)
          iat → 발급시간(Issued At)
          공개 클레임(Public Claims): 개발자가 자유롭게 정의 가능
          비공개 클레임(Private Claims): 시스템 내부에서만 사용
        */
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username) // JWT 페이로드의 sub(주체) 필드에 username 저장
                .setIssuedAt(now)// iat 발행 시간
                .setExpiration(new Date(now.getTime() + expireMillis))// exp 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)// 비밀키로 HMAC-SHA256 서명
                .compact();  // 문자열로 변환
    }

    /*
     JWT를 파싱해서 subject를 가져옴
     예외 발생 시 JWT가 잘못된 것이므로 보통 호출하는 쪽에서 예외 처리 필요
    */
    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /*
      토큰을 파싱해 문제가 없으면 true, 예외 발생 시 false
      검증 항목: 서명 유효성, 만료 시간, 토큰 구조
    */
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public void addTokensToCookie(HttpServletResponse response, String accessToken, String refreshToken) {
        addCookie(response, accessTokenName, accessToken, accessCookieMaxAge, "/");
        addCookie(response, refreshTokenName, refreshToken, refreshCookieMaxAge, "/auth/refresh");
    }

    public void addNewTokensToCookie(HttpServletResponse response, String accessToken) {
        addCookie(response, accessTokenName, accessToken, accessCookieMaxAge, "/");
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true); //HttpOnly → JS에서 접근 불가, XSS 공격 방어
        //cookie.setSecure(true); // HTTPS 환경 필수
        cookie.setPath(path); // 쿠키 유효 경로
        cookie.setMaxAge(maxAge); // 만료 시간
        cookie.setAttribute("SameSite", "Strict"); //CSRF 위험방지
        response.addCookie(cookie);
    }

    public void deleteCookie(HttpServletResponse response) {
        Cookie accessCookie = new Cookie(accessTokenName, null);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0); //MaxAge=0 → 쿠키 즉시 삭제
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie(refreshTokenName, null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/auth/refresh");
        refreshCookie.setMaxAge(0); //MaxAge=0 → 쿠키 즉시 삭제
        response.addCookie(refreshCookie);
    }
}
