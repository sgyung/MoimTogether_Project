package com.project.moimtogether.util;

import jakarta.servlet.http.HttpServletRequest;

public class HeaderUtils {
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer "; // 띄어쓰기 포함
    private final static String HEADER_REFRESH_TOKEN = "RefreshToken";

    // Access Token 추출
    public static String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION);
        // 요청에서 Authorization 헤더 값 가져와서

        // null인 경우 null 반환
        if (headerValue == null) {
            return null;
        }

        // 존재하는데 Bearer로 시작한다면
        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
            // 해당 문자열 제거한 나머지 부분을 엑세스 토큰으로 간주하여 반환
            // substring(TOKEN_PREFIX.length()) --> TOKEN_PREFIX의 길이부터 문자열의 끝까지의 부분 문자열 반환
        }

        // Bearer로 시작하지 않는다면 null 반환
        return null;
    }

    // Refresh Token 추출
    public static String getHeaderRefreshToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_REFRESH_TOKEN);
        // 요청에서 refresh token 값 가져와서

        // null인 경우 null 반환
        if (headerValue == null) {
            return null;
        }

        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
