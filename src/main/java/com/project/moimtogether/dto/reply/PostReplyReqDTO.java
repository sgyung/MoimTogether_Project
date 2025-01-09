package com.project.moimtogether.dto.reply;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "댓글 등록 요청 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReplyReqDTO {

    @NotNull(message = "공지사항 아이디는 공백일 수 없습니다.")
    private Long notifyId;

    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z0-9!@#$%^&*()_+=\\\\.\\s?]{1,100}$", message = "댓글 내용은 최대 100자까지 입력 가능합니다.")
    private String replyContent;
}
