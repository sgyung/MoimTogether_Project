package com.project.moimtogether.dto.review;

import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.place.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(name = "리뷰 정보 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostReviewReqDTO {

    @NotNull(message = "장소 아이디는 공백일 수 없습니다.")
    private Long placeId;

    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z0-9!@#$%^&*()_+=\\.\\s]{1,150}$", message = "리뷰 내용은 최대 150자까지 입력 가능합니다.")
    private String reviewContent;

}
