package com.project.moimtogether.dto.reply;

import com.project.moimtogether.domain.reply.Reply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "댓글 정보 객체")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyDTO {

    private Long replyId;
    private String replyContent;
    private LocalDateTime replyDate;
    private String memberId;

    public ReplyDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.replyContent = reply.getReplyContent();
        this.replyDate = reply.getReplyDate();
        this.memberId = reply.getMember().getMemberId();
    }
}
