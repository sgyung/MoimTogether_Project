package com.project.moimtogether.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "아이디 찾기 요청 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindLoginIdReqDTO {
    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,10}$", message = "이름 형식이 유효하지 않습니다.")
    private String memberName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식이 유효하지 않습니다.")
    private String memberEmail;

    @NotBlank
    @Pattern(regexp = "^01(0|1|[6-9])[0-9]{3,4}[0-9]{4}$", message = "전화번호 형식이 유효하지 않습니다. ex)0101231234")
    private String memberPhone;
}
