package com.project.moimtogether.dto.meeting;

import com.project.moimtogether.domain.meeting.Meeting;
import com.project.moimtogether.domain.meetingroom.MeetingRoomRole;
import com.project.moimtogether.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "특정 모임방 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRoomDTO {

    private Member member;
    private Meeting meeting;
    private MeetingRoomRole meetingRoomRole;
    private LocalDateTime meetingRoomJoinDate;
}
