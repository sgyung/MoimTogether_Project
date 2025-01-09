package com.project.moimtogether.dto.review;

import com.project.moimtogether.domain.place.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(name = "리뷰 수정 응답 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModifyReviewResDTO {
    private Long reviewId;
    private String reviewContent;
    private LocalDateTime reviewDate;
}
