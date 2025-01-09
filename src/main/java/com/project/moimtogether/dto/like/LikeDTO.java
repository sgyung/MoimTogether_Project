package com.project.moimtogether.dto.like;

import com.project.moimtogether.domain.like.LikeStatus;
import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.place.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "좋아요 생성 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDTO {

    private Member member;
    private Place place;
    private LikeStatus likeStatus;
}
