package com.project.moimtogether.dto.review;

import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.place.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(name = "리뷰 정보 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {

    private String reviewContent;
    private Member member;
    private Place place;
}
