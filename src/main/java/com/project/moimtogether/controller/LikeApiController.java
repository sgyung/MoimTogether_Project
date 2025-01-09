package com.project.moimtogether.controller;

import com.project.moimtogether.config.error.ErrorResponse;
import com.project.moimtogether.config.error.customException.SuccessCode;
import com.project.moimtogether.dto.like.CancelLikeReqDTO;
import com.project.moimtogether.dto.like.PostLikeResDTO;
import com.project.moimtogether.service.like.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class LikeApiController {

    private final LikeService likeService;

    @PostMapping("/likes")
    @Operation(summary = "장소에 좋아요를 누를 수 있습니다.")
    public ResponseEntity<ErrorResponse<PostLikeResDTO>> registerLike(@RequestBody Map<String, Long> placeId){

        Long id = placeId.get("placeId");

        PostLikeResDTO postLikeResDTO = likeService.registerLike(id);

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_LIKE_SUCCESS, postLikeResDTO));
    }

    @PatchMapping("likes")
    @Operation(summary = "장소에 좋아요를 취소합니다.")
    public ResponseEntity<ErrorResponse<String>> cancelLike(@Valid @RequestBody CancelLikeReqDTO reqDTO){
        likeService.cancelLike(reqDTO);

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_CANCEL_LIKE_SUCCESS,"취소 완료"));
    }
}
