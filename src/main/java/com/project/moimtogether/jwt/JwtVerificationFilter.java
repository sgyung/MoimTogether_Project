package com.project.moimtogether.jwt;

import com.project.moimtogether.util.HeaderUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final AuthTokenProvider authTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenStr = HeaderUtils.getAccessToken(request); // AccessToken 추출해서 Bearer 제외하고 나머지 토큰으로 인식한 후에
        AuthToken token = authTokenProvider.convertAuthToken(tokenStr); // 해당 str로 토큰으로 변환


        // securityContextHolder에 유저 정보를 저장해주는 로직
        if (token.isTokenValid()) {
            Authentication authentication = authTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
