package com.project.moimtogether.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "회원 기본 정보 응답 객체")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseMemberDTO {
    private String memberId;
    private String memberName;
}

