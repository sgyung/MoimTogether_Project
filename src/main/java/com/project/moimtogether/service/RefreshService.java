package com.project.moimtogether.service;

import com.project.moimtogether.config.error.customException.CustomLogicException;
import com.project.moimtogether.config.error.customException.ExceptionCode;
import com.project.moimtogether.domain.RefreshToken;
import com.project.moimtogether.jwt.AuthToken;
import com.project.moimtogether.jwt.AuthTokenProvider;
import com.project.moimtogether.util.HeaderUtils;
import com.project.moimtogether.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthTokenProvider authTokenProvider;

    // 리프레시 토큰 저장
    public void saveRefreshToken(String userId, AuthToken authToken) {
        refreshTokenRepository.findByUserId(userId) // 해당 이메일에 대한 리프레시 토큰 조회
                .ifPresentOrElse( // 존재한다면 리프레시 토큰 업데이트 후 저장
                        refreshToken -> {
                            // 토큰과 만료 날짜 새로 업데이트
                            refreshToken.setRefreshToken(authToken.getToken());
                            refreshToken.setExpiryDate(authToken.getValidTokenClaims().getExpiration());
                        },
                        () -> {
                            RefreshToken refreshToken = RefreshToken.builder()
                                    .userId(userId)
                                    .refreshToken(authToken.getToken())
                                    .expiryDate(authToken.getValidTokenClaims().getExpiration())
                                    .build();
                            refreshTokenRepository.save(refreshToken);
                        }
                );
    }

    // 리프레시 토큰으로 액세스 토큰 갱신
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        AuthToken accessToken = authTokenProvider.convertAuthToken(HeaderUtils.getAccessToken(request));
        validateAccessTokenCheck(accessToken);
        String userId = accessToken.getExpiredTokenClaims().getSubject();
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomLogicException(ExceptionCode.REFRESH_TOKEN_NOT_FOUND));
        validateRefreshTokenCheck(refreshToken, authTokenProvider.convertAuthToken(HeaderUtils.getHeaderRefreshToken(request)));

        // 새 엑세스 토큰 생성
        AuthToken newAccessToken = authTokenProvider.createAccessToken(userId);

        response.addHeader("Authorization", "Bearer " + newAccessToken.getToken());
    }

    // 엑세스 토큰 유효성 확인
    public void validateAccessTokenCheck(AuthToken authToken) {
        if (!authToken.isTokenExpired()) // 만료되지 않았다면 에러 (만료가 되어야 리프레시 토큰으로 다시 발급받을 수 있기 때문)
            throw new CustomLogicException(ExceptionCode.TOKEN_INVALID);

        if (authToken.getExpiredTokenClaims() == null) // 만료된 토큰의 클레임이 null인지 확인
            throw new CustomLogicException(ExceptionCode.TOKEN_INVALID);
        // 토큰이 만료되었을 때, 해당 토큰의 클레임을 가져올 수 있다면 이는 토큰이 잘못된 것으로 간주
    }

    // 리프레시 토큰 유효성 검증
    public void validateRefreshTokenCheck(RefreshToken refreshToken, AuthToken headerRefreshToken) {
        if(!headerRefreshToken.isTokenValid()) // 만료되었다면 에러
            throw new CustomLogicException(ExceptionCode.REFRESH_TOKEN_INVALID);

        if (!refreshToken.getRefreshToken().equals(headerRefreshToken.getToken())) // 리프레시 토큰이 같지 않다면 에러
            throw new CustomLogicException(ExceptionCode.REFRESH_TOKEN_NOT_MATCH);
    }
}
