package com.project.moimtogether.service.member;

import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.jwt.AuthToken;
import com.project.moimtogether.jwt.AuthTokenProvider;
import com.project.moimtogether.service.RefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthTokenProvider authTokenProvider;
    private final RefreshService refreshService;

    public List<AuthToken> login(Member member) {
        AuthToken accessToken = authTokenProvider.createAccessToken(member.getMemberId());
        AuthToken refreshToken = authTokenProvider.createRefreshToken(member.getMemberId());
        refreshService.saveRefreshToken(member.getMemberId(), refreshToken);
        return List.of(accessToken, refreshToken);
    }
}
