package com.project.moimtogether.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "회원 정보 응답 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostMemberResDTO {

    private String memberId;
    private String memberName;
    private String memberBirth;
    private String memberEmail;
    private String memberGender;
    private String memberPhone;
    private String profilePath;
    private String joinDate;
}
