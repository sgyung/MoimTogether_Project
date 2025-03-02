package com.project.moimtogether.config;

import com.project.moimtogether.jwt.AuthTokenProvider;
import com.project.moimtogether.jwt.JwtVerificationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthTokenProvider authTokenProvider;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean // 비밀번호 암호화 메소드
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // JwtVerificationFilter 설정
        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(authTokenProvider);

        // CSRF 설정 비활성화
        http
                .csrf(AbstractHttpConfigurer::disable);

        // Form 로그인 비활성화
        http
                .formLogin(AbstractHttpConfigurer::disable);

        // HTTP Basic 인증 비활성화
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        // 경로별 접근 권한 설정
        http
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**",
                        "/api/sign-up",
                        "/api/login",
                        "/",
                        "/v3/api-docs/**",
                        "/api/seoul-data",
                        "/api/places",
                        "/api/environment",
                        "/actuator/health").permitAll() // 인증 필요 없음
                .anyRequest().authenticated() // 나머지는 인증 필요
        );

        // 세션 설정 (STATELESS)
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 필터 설정: JwtVerificationFilter 추가
        http
                .addFilterBefore(new JwtVerificationFilter(authTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        // 로그아웃 설정
        http
                .logout(logout -> logout.logoutUrl("/api/logout"));

        return http.build();
    }
}
