package com.project.moimtogether.domain.review;

import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.place.Place;
import com.project.moimtogether.dto.review.ModifyReviewReqDTO;
import com.project.moimtogether.dto.review.ReviewDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "review")
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "review_content")
    private String reviewContent;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;


    // 생성 메서드
    public static Review createReview(ReviewDTO reviewDTO){
        Review review = new Review();
        review.setReviewContent(reviewDTO.getReviewContent());
        review.setReviewDate(LocalDateTime.now());
        review.setMember(reviewDTO.getMember());
        review.setPlace(reviewDTO.getPlace());
        reviewDTO.getPlace().addReview(review);
        return review;
    }

    // 업데이트 메서드
    public void updateReview(ModifyReviewReqDTO reqDTO){
        if(reqDTO.getReviewContent() != null){
            this.reviewContent = reqDTO.getReviewContent();
        }
        reviewDate = LocalDateTime.now();
    }
}
