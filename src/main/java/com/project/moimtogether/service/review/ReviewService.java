package com.project.moimtogether.service.review;

import com.project.moimtogether.config.error.customException.PlaceException;
import com.project.moimtogether.config.error.customException.UserException;
import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.member.UserErrorCode;
import com.project.moimtogether.domain.place.Place;
import com.project.moimtogether.domain.place.PlaceErrorCode;
import com.project.moimtogether.domain.review.Review;
import com.project.moimtogether.dto.review.*;
import com.project.moimtogether.repository.PlaceRepository;
import com.project.moimtogether.repository.ReviewRepository;
import com.project.moimtogether.repository.UserRepository;
import com.project.moimtogether.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    // 리뷰 등록 서비스 메서드
    public PostReviewResDTO registerReviewService(PostReviewReqDTO reqDTO) {
        Member currentMember = SecurityUtils.getCurrentMember();
        if(currentMember == null) {
            throw new UserException(UserErrorCode.NOT_LOGIN_ERROR);
        }

        Place currentPlace = placeRepository.findById(reqDTO.getPlaceId())
                .orElseThrow(() -> new PlaceException(PlaceErrorCode.PLACE_EMPTY_ERROR));

        Member member = userRepository.findByMemberId(currentMember.getMemberId())
                .orElseThrow(() -> new UserException(UserErrorCode.ID_NOT_EXISTS));

        Review existingReview = reviewRepository.findReviewByPlaceAndMember(currentPlace, member);

        if(existingReview != null) {
            throw new PlaceException(PlaceErrorCode.REVIEW_EXISTS_ERROR);
        }

        try {
            Review review = Review.createReview(new ReviewDTO(reqDTO.getReviewContent(), member, currentPlace));
            reviewRepository.save(review);

            return PostReviewResDTO.builder()
                    .reviewId(review.getId())
                    .reviewContent(review.getReviewContent())
                    .reviewDate(review.getReviewDate())
                    .memberId(member.getMemberId())
                    .placeId(currentPlace.getId())
                    .placeName(currentPlace.getPlaceName())
                    .build();

        }catch (Exception e){
            throw new PlaceException(PlaceErrorCode.UNEXPECTED_ERROR);
        }
    }

    // 리뷰 수정 서비스 메서드
    public ModifyReviewResDTO modifyReviewService(ModifyReviewReqDTO reqDTO) {
        Member currentMember = SecurityUtils.getCurrentMember();
        if(currentMember == null) {
            throw new UserException(UserErrorCode.NOT_LOGIN_ERROR);
        }

        Review review = reviewRepository.findById(reqDTO.getReviewId())
                .orElseThrow(() -> new PlaceException(PlaceErrorCode.REVIEW_ID_ERROR));

        review.updateReview(reqDTO);

        try {
            reviewRepository.save(review);

            return ModifyReviewResDTO.builder()
                    .reviewId(review.getId())
                    .reviewContent(review.getReviewContent())
                    .reviewDate(review.getReviewDate())
                    .build();

        }catch (Exception e){
            throw new PlaceException(PlaceErrorCode.UNEXPECTED_ERROR);
        }
    }

    // 리뷰 삭제 서비스 메서드
    public void deleteReviewService(Long reviewId) {
        Member currentMember = SecurityUtils.getCurrentMember();
        if(currentMember == null) {
            throw new UserException(UserErrorCode.NOT_LOGIN_ERROR);
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new PlaceException(PlaceErrorCode.REVIEW_ID_ERROR));

        try {
            reviewRepository.delete(review);
        }catch (Exception e){
            throw new PlaceException(PlaceErrorCode.UNEXPECTED_ERROR);
        }

    }
}
