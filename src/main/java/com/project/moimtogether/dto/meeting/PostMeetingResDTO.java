package com.project.moimtogether.dto.meeting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "모임 정보 응답 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostMeetingResDTO {

    private Long meetingId; //모임 아이디
    private String meetingTitle; //모임 제목
    private int maximumPerson; //모임 최대 인원
    private String meetingDate; //모임 날짜
    private String meetingTime; //모임 시간
    private String meetingDescription; //모임 설명
    private String meetingStatus; //모임 상태
    private String meetingLeader; //모임 리더
    private String meetingCreateTime; //모임 생성 시간

}
