package com.project.moimtogether.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "회원 상세 정보 응답 객체")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MemberInfoDTO extends BaseMemberDTO {
    private String memberBirth;
    private String memberEmail;
    private String memberPhone;
    private String profile;
}
