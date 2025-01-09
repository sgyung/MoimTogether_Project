package com.project.moimtogether.dto.reply;

import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.notify.Notify;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "댓글 등록 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterReplyDTO {

    private String replyContent;
    private Notify notify;
    private Member member;
}
