package com.project.moimtogether.dto.alarm;

import com.project.moimtogether.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "알람 정보 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmDTO {

    private String title;
    private String content;
    private Member member;

}
