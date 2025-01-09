package com.project.moimtogether.dto.meeting;

import com.project.moimtogether.domain.meetingroom.MeetingRoomRole;
import com.project.moimtogether.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "모임 인원 정보 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRoomMemberDTO {

    private Member member;
    private MeetingRoomRole role;
    private LocalDateTime joinDateTime;
}
