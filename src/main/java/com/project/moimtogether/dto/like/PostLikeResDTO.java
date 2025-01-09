package com.project.moimtogether.dto.like;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "좋아요 응답 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLikeResDTO {

    private Long LikeId;
    private String memberId;
    private Long placeId;
    private String placeName;
}
