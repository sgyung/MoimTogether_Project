package com.project.moimtogether.controller;

import com.project.moimtogether.config.error.ErrorResponse;
import com.project.moimtogether.config.error.customException.SuccessCode;
import com.project.moimtogether.dto.review.ModifyReviewReqDTO;
import com.project.moimtogether.dto.review.ModifyReviewResDTO;
import com.project.moimtogether.dto.review.PostReviewReqDTO;
import com.project.moimtogether.dto.review.PostReviewResDTO;
import com.project.moimtogether.service.review.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
@Tag(name = "[리뷰 관련 API]")
public class ReviewApiController {
    private final ReviewService reviewService;

    // 리뷰 작성 컨트롤러 메서드
    @PostMapping("/reviews")
    @Operation(summary = "장소에 리뷰를 작성할 수 있습니다.")
    public ResponseEntity<ErrorResponse<PostReviewResDTO>> registerReview(@Valid @RequestBody PostReviewReqDTO postReviewReqDTO) {
        PostReviewResDTO resDTO = reviewService.registerReviewService(postReviewReqDTO);

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.REVIEW_INSERT_SUCCESS, resDTO));
    }

    // 리뷰 수정 컨트롤러 메서드
    @PatchMapping("/reviews")
    @Operation(summary = "리뷰를 수정할 수 있습니다.")
    public ResponseEntity<ErrorResponse<ModifyReviewResDTO>> modifyReview(@Valid @RequestBody ModifyReviewReqDTO modifyReviewReqDTO) {
        ModifyReviewResDTO resDTO = reviewService.modifyReviewService(modifyReviewReqDTO);

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_MODIFY_SUCCESS, resDTO));
    }

    // 리뷰 삭제 컨트롤러 메서드
    @DeleteMapping("/reviews")
    @Operation(summary = "리뷰를 삭제할 수 있습니다.")
    public ResponseEntity<ErrorResponse<String>> deleteReview(@RequestBody Map<String, Long> placeId) {
        reviewService.deleteReviewService(placeId.get("placeId"));

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_DELETE_SUCCESS, "삭제 성공"));
    }
}
