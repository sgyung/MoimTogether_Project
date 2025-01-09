package com.project.moimtogether.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "비밀번호 재설정 요청 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordVerificationReqDTO {

    @NotBlank(message = "공백일 수 없습니다.")
    private String temporaryPassword;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$",
    message = "비밀번호는 최소 8자 이상이며, 영문/숫자/특수문자를 각각 하나 이상 포함해야 합니다.")
    private String memberPassword;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$",
    message = "비밀번호는 최소 8자 이상이며, 영문/숫자/특수문자를 각각 하나 이상 포함해야 합니다.")
    private String memberPasswordCheck;
}
