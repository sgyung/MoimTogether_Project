package com.project.moimtogether.jwt;

import com.project.moimtogether.config.error.customException.CustomLogicException;
import com.project.moimtogether.config.error.customException.ExceptionCode;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
@Getter
public class AuthToken {

    private String token;
    private String tokenType;
    private Key key;

    // 생성자 1 - 기본 생성자
    public AuthToken(String token, Key key) {
        this.key = key;
        this.token = token;
    }

    // 생성자 2
    public AuthToken(String id, String tokenType, Date expiry, Key key) {
        this.key = key;
        this.tokenType = tokenType;
        this.token = createAccessToken(id, expiry);
    }

    // id와 만료 기한으로 accessToken 생성
    private String createAccessToken(String id, Date expiry) {
        return Jwts.builder()
                .setSubject(id) // JWT의 "sub" (subject) 클레임에 사용자 id를 설정 ( 사용자 고유 식별자 )
                .signWith(key, SignatureAlgorithm.HS256) // HS256 알고리즘과 key를 사용하여 jwt 서명
                // ( 서명 - JWT가 변경되지 않았음을 보장하고, 무결성을 유지하기 위해 사용 )
                .setExpiration(expiry) // 만료 기한 설정
                .compact(); // jwt를 문자열로 변환
    }

    // 토큰이 유효한지 여부 검증
    public boolean isTokenValid() {
        return getValidTokenClaims() != null; // 유효하면 true
    }
    // 토큰의 만료 여부 검증
    public boolean isTokenExpired() {
        return getExpiredTokenClaims() != null; // 만료됐으면 true
    }

    // 만료되지 않은 토큰의 클레임 추출
    // JWT 구문 분석 과정에서 예외 발생 시, 해당 예외 처리 후 null 반환
    public Claims getValidTokenClaims() {
        try {
            return Jwts.parser() // jwt 구문 분석을 위한 builder 객체 생성
                    .setSigningKey(key) // JWT의 서명을 검증할 때 사용할 서몀키. 생성할 때 사용한 키와 동일한 키를 사용
                    .build() // 실제 jwt 파서 객체 생성
                    .parseClaimsJws(token) // 생성된 jwt 파서를 사용해서 주어진 토큰 구문 분석 ( 여기서 서명이 유효한지 확인 )
                    .getBody(); // 구문 분석된 jwt 클레임(본문) 반환
        } catch (MalformedJwtException e) { // jwt 토큰 형식이 올바르지 않을 경우 발생
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) { // jwt 토큰의 유효 기간이 만료된 경우 발생
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) { // jwt 토큰이 지원되지 않는 형식이거나, 지원되지 않는 기능을 사용할 경우 발생
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) { // 잘못된 인자가 전달되었을 경우 발생
            log.info("JWT token compact of handler are invalid.");
        } catch (io.jsonwebtoken.security.SignatureException e) { // jwt 토큰의 서명이 유효하지 않은 경우 발생
            throw new CustomLogicException(ExceptionCode.TOKEN_INVALID);
        }
        return null;
    }

    // 만료된 토큰의 클레임 추출
    public Claims getExpiredTokenClaims() {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            return e.getClaims();
            // 예외가 발생한 경우 (jwt 토큰의 만료 기간이 지났을 경우)
            // 로그 출력 후, 토큰의 클레임 대신 예외에서 가져온 클레임 반환
        }
        return null; // 예외가 발생하지 않는다면 null 반환
    }

}
