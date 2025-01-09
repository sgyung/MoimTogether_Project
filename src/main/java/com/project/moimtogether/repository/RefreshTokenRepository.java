package com.project.moimtogether.repository;

import com.project.moimtogether.domain.RefreshToken;
import com.project.moimtogether.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(String userId);

}
