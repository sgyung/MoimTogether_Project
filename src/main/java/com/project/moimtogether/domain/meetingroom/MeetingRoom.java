package com.project.moimtogether.domain.meetingroom;

import com.project.moimtogether.domain.meeting.Meeting;
import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.dto.meeting.MeetingRoomDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "meeting_room")
public class MeetingRoom {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "meeting_room_id", nullable = false)
    private Long meetingRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_room_role", nullable = false)
    private MeetingRoomRole meetingRoomRole;

    @Column(name = "meeting_room_join_date", nullable = false)
    private LocalDateTime meetingRoomJoinDate;

    //==생성 메서드==//
    public static MeetingRoom joinMeetingRoom(MeetingRoomDTO meetingRoomDTO) {
        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.setMember(meetingRoomDTO.getMember());
        meetingRoom.setMeeting(meetingRoomDTO.getMeeting());
        meetingRoom.setMeetingRoomRole(meetingRoomDTO.getMeetingRoomRole());
        meetingRoom.setMeetingRoomJoinDate(meetingRoomDTO.getMeetingRoomJoinDate());

        return meetingRoom;
    }

}
