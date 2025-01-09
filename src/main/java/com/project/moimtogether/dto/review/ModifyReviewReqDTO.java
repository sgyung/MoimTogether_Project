package com.project.moimtogether.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "리뷰 수정 요청 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModifyReviewReqDTO {

    @NotNull(message = "리뷰 아이디는 공백일 수 없습니다.")
    private Long reviewId;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9!@#$%^&*()_+=\\.\\s]{1,150}$", message = "리뷰 내용은 최대 150자까지 입력 가능합니다.")
    private String reviewContent;

}
