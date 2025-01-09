package com.project.moimtogether.dto.place;

import com.project.moimtogether.domain.review.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "장소 리뷰 정보 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceReviewDTO {

    private Long reviewId;
    private String reviewContent;
    private LocalDateTime reviewDate;
    private String memberId;

    public PlaceReviewDTO(Review review) {
        this.reviewId = review.getId();
        this.reviewContent = review.getReviewContent();
        this.reviewDate = review.getReviewDate();
        this.memberId = review.getMember().getMemberId();
    }

}
