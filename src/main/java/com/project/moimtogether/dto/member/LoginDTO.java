package com.project.moimtogether.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 요청 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    private String memberId;
    private String memberPassword;
}
