package com.project.moimtogether.dto.member;

import com.project.moimtogether.domain.member.UserGender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "회원가입 요청 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMemberReqDTO {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "아이디는 최소 4자 이상이며, 영문 또는 숫자만 사용 가능합니다.")
    private String memberId;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$",
    message = "비밀번호는 최소 8자 이상이며, 영문/숫자/특수문자를 각각 하나 이상 포함해야 합니다.")
    private String memberPassword;

    @NotBlank(message = "비밀번호 확인란은 공백일 수 없습니다.")
    private String memberPasswordCheck;

    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,10}$",message = "이름 형식이 유효하지 않습니다.")
    private String memberName;

    @NotBlank
    @Pattern(regexp = "^\\d{8}$", message = "생년월일 형식이 유효하지 않습니다. ex)19900101")
    private String memberBirth;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식이 유효하지 않습니다.")
    private String memberEmail;

    @NotNull
    private UserGender memberGender;

    @NotBlank
    @Pattern(regexp = "^01(0|1|[6-9])[0-9]{3,4}[0-9]{4}$", message = "전화번호 형식이 유효하지 않습니다. ex)0101231234")
    private String memberPhone;
}
