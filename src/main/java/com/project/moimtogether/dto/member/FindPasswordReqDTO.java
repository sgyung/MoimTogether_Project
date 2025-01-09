package com.project.moimtogether.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "비밀번호 찾기 요청 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindPasswordReqDTO{

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "아이디는 최소 4자 이상이며, 영문 또는 숫자만 사용 가능합니다.")
    private String memberId;

    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,10}$", message = "이름 형식이 유효하지 않습니다.")
    private String memberName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식이 유효하지 않습니다.")
    private String memberEmail;
}
