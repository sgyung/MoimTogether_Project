package com.project.moimtogether.jwt;

import com.project.moimtogether.config.error.customException.CustomLogicException;
import com.project.moimtogether.config.error.customException.ExceptionCode;
import com.project.moimtogether.dto.member.CustomUserDetailsDTO;
import com.project.moimtogether.service.member.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Slf4j
public class AuthTokenProvider {

    private final String ACCESS_TOKEN = "AccessToken";
    private final String REFRESH_TOKEN = "RefreshToken";

    private final Key key;
    private final long tokenValidTime;
    private final long refreshTokenValidTime;
    private final CustomUserDetailService customUserDetailService;

    public AuthTokenProvider(String secret, long tokenValidTime, long refreshTokenValidTime, CustomUserDetailService customUserDetailService) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        // 주어진 비밀키를 사용하여 HMAC-SHA 기반의 키 생성
        this.tokenValidTime = tokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
        this.customUserDetailService = customUserDetailService;
    }

    // 액세스 토큰 생성
    public AuthToken createAccessToken(String id) {
        return new AuthToken(id,ACCESS_TOKEN,new Date(System.currentTimeMillis() + tokenValidTime), key);
    }

    // 리프레시 토큰 생성
    public AuthToken createRefreshToken(String id) {
        return new AuthToken(id, REFRESH_TOKEN, new Date(System.currentTimeMillis() + refreshTokenValidTime), key);
    }

    // 토큰 문자열로 AuthToken 객체 만드는 메서드 ( 토큰 검증 및 관련 작업 가능 )
    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    // Authentication 객체 생성 메서드
    public Authentication getAuthentication(AuthToken authToken) {
        if (authToken.isTokenValid()) { // 토큰 유효성 확인
            Claims claims = authToken.getValidTokenClaims();

            log.debug("claims subject := [{}]", claims.getSubject());

            // loadUserByUsername 메서드에서 만든 userDetails 생성
            CustomUserDetailsDTO userDetails = customUserDetailService.loadUserByUsername(authToken.getValidTokenClaims().getSubject());

            // 생성된 User 객체와 권한 정보를 사용하여 UsernamePasswordAuthenticationToken 생성
            return new UsernamePasswordAuthenticationToken(userDetails, authToken, Collections.emptyList());
        }
        else {
            throw new CustomLogicException(ExceptionCode.USER_NONE);
            // 여기서 USER_NONE 을 던지는 이유는 이미 isTokenValid 메서드에서 검증할 때, 여러 유효성 검사(만료 등)를 거쳤기 때문에
            // 다른 부분에서 유효성 검사에 걸렸다면 그 익셉션이 발생했을 것.
            // 여기까지 왔다면 유저가 없는 것 밖에 없음
        }
    }
}
