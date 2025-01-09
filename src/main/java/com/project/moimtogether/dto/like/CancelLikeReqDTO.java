package com.project.moimtogether.dto.like;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "좋아요 취소 요청 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelLikeReqDTO {

    @NotNull(message = "장소 아이디는 공백일 수 없습니다.")
    private Long placeId;

    @NotNull(message = "좋아요 아이디는 공백일 수 없습니다.")
    private Long likeId;
}
