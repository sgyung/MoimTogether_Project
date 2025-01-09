package com.project.moimtogether.repository;

import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.dto.member.FindLoginIdReqDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String loginId);
    Optional<Member> findByMemberNameAndMemberEmailAndMemberPhone(String memberName, String memberEmail, String memberPhone);
}
