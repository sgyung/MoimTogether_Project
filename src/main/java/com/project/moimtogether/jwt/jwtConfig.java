package com.project.moimtogether.jwt;

import com.project.moimtogether.service.member.CustomUserDetailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@RequiredArgsConstructor
public class jwtConfig {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long tokenValidTime;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenValidTime;

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public AuthTokenProvider authTokenProvider() {
        return new AuthTokenProvider(secret, tokenValidTime, refreshTokenValidTime,customUserDetailService);
    }
}
