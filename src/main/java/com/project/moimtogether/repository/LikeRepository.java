package com.project.moimtogether.repository;

import com.project.moimtogether.domain.like.Likes;
import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes,Long> {

    Optional<Likes> findByMemberAndPlace(Member member, Place place);

}
