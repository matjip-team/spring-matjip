package com.restaurant.matjip.global.config;

import com.restaurant.matjip.global.component.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * SecurityConfig 클래스
 * - 모든 HTTP 요청에 대한 보안 필터를 적용
 * - SecurityFilterChain 빈 하나가 Spring Security 필터 체인에 등록되어, 요청마다 실행
 *
 * 작성자: Shawn Lee
 * 작성일: 2026-01-28
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //Spring Security 필터로, 요청마다 JWT를 확인하고 인증 정보를 SecurityContext에 세팅하는 역할
    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * HttpSecurity는 웹 보안 설정을 정의할 때 사용하는 객체입니다.
     * HTTP 요청이 들어왔을 때 어떤 요청이 인증/인가가 필요한지,
     * 어떤 로그인/로그아웃 방식을 사용할지,
     * CSRF, CORS, 세션 관리 등 웹 관련 보안 설정을 담당합니다.
     *
     * @param http 웹 보안 설정
     * @return 설정한 필터체인
     * @throws Exception 예외 발생 상황 설명
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
          http 설정
          1. CORS(Cross-Origin Resource Sharing)
             - 같은 출처(Scheme + Host + Port)에서만 리소스를 요청할 수 있음
          2. CSRF(Cross-Site Request Forgery)
             - CSRF는 웹 보안 공격 중 하나
             - 공격자가 사용자가 로그인한 상태를 이용해 사용자 모르게 악의적 요청을 서버로 보내는 공격
          3. SessionCreationPolicy.STATELESS
             - 각 요청은 독립적이며, 인증 정보도 JWT, API 토큰 등으로 요청마다 전달해야 함
          4. UsernamePasswordAuthenticationFilter
             - Spring Security에서 기본 로그인 처리 필터
             - 인증 성공 → SecurityContext에 인증 정보 저장
        */
        http.cors(Customizer.withDefaults()) //기본설정을 사용하되 @CrossOrigin 애노테이션 -> CorsConfigurationSource Bean이 있다면 그 설정 사용 없으면 기본값 적용(모든 요청 허용 X, 제한적)
                .csrf(AbstractHttpConfigurer::disable)  //JWT는 stateless 인증이므로 CSRF 보호가 필요 없음 → 비활성화
                //JWT 기반 인증은 세션을 사용하지 않음 → Stateless 서버는 인증 상태를 저장하지 않고, 매 요청마다 JWT 검증
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth ->
                        auth
                                // 1. 관리자 먼저 (구체적인 것부터)
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                // 2. 보호할 auth 경로
                                .requestMatchers(
                                        "/api/register/**",
                                    "/api/auth/mypage/**"
                                ).authenticated()
                                // 3. 공개 경로
                                .requestMatchers(
                                        "/**"
                                ).permitAll()
                                // 4. 나머지는 로그인 필요
                                .anyRequest().authenticated()
                )
                //Spring Security의 기본 UsernamePasswordAuthenticationFilter 전에 jwtFilter를 실행
                //즉, 요청이 들어오면 JWT 확인 후 SecurityContext에 인증 정보를 세팅하고 이후 필터 진행
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //TODO : 패스워드 임시
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        //return new BCryptPasswordEncoder();
    }

    @Bean // CorsConfigurationSource는 다른 도메인(origin)에서 자원 요청시 브라우저가 허용 여부를 검사해주는 보안 정책입니다.
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // 리액트 주소
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // 허용할 메소드 목록
        // 클라이언트가 서버에 요청시 모든 요청 정보를 허용하겠습니다.(JWT 쓰면 이설정 필수)
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // 쿠키, 세션 인증 정보 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source ;
    }

    
}
