package com.project.moimtogether.dto.notify;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "공지사항 등록 요청 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostNotifyReqDTO {

    @NotNull(message = "모임 아이디는 공백일 수 없습니다.")
    private Long meetingId;

    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z0-9!@#$%^&*()_+=\\.\\s]{1,30}$", message = "공지사항 제목은 최대 30자까지 입력 가능합니다.")
    private String notifyTitle;

    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z0-9!@#$%^&*()_+=\\.\\s]{1,150}$", message = "공지사항 내용은 최대 150자까지 입력 가능합니다.")
    private String notifyContent;
}
