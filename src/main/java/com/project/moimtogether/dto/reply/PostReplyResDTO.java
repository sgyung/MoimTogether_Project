package com.project.moimtogether.dto.reply;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;

@Schema(description = "댓글 등록 응답 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostReplyResDTO {

    private Long notifyId;
    private Long replyId;
    private String replyContent;
    private LocalDateTime replyDate;

}
