package com.project.moimtogether.domain.meeting;

import com.project.moimtogether.domain.meetingroom.MeetingRoom;
import com.project.moimtogether.domain.place.Place;
import com.project.moimtogether.dto.meeting.MeetingDTO;
import com.project.moimtogether.dto.meeting.ModifyMeetingReqDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "meeting")
public class Meeting {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id", nullable = false)
    private Long meetingId;

    @Column(name = "meeting_title", nullable = false)
    private String meetingTitle;

    @Column(name = "maximum_person", nullable = false)
    private Integer maximumPerson;

    @Column(name = "meeting_description", nullable = false)
    private String meetingDescription;

    @Column(name = "meeting_date", nullable = false)
    private LocalDate meetingDate;

    @Column(name = "meeting_time", nullable = false)
    private LocalTime meetingTime;

    @Column(name = "meeting_leader", nullable = false)
    private String meetingLeader;

    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_status", nullable = false)
    private MeetingStatus meetingStatus;

    @Column(name = "meeting_create_time", nullable = false)
    private LocalDateTime meetingCreateTime;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<MeetingRoom> meetingRooms = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    // 생성 메서드
    public static Meeting createMeeting(MeetingDTO meetingDTO){
        Meeting meeting = new Meeting();
        meeting.setMeetingTitle(meetingDTO.getMeetingTitle());
        meeting.setMaximumPerson(Integer.parseInt(meetingDTO.getMaximumPerson()));
        meeting.setMeetingDescription(meetingDTO.getMeetingDescription());
        meeting.setMeetingDate(LocalDate.parse(meetingDTO.getMeetingDate()));
        meeting.setMeetingTime(LocalTime.parse(meetingDTO.getMeetingTime()));
        meeting.setMeetingLeader(meetingDTO.getMeetingLeader());
        meeting.setMeetingStatus(meetingDTO.getMeetingStatus());
        meeting.setMeetingCreateTime(LocalDateTime.now());
        meeting.setPlace(meetingDTO.getMeetingPlace());
        return meeting;
    }

    // 모임 수정 메서드
    public void modifyMeeting(ModifyMeetingReqDTO modifyMeetingReqDTO) {
        if(modifyMeetingReqDTO.getMeetingTitle() != null){
            this.meetingTitle = modifyMeetingReqDTO.getMeetingTitle();
        }
        if(modifyMeetingReqDTO.getMaximumPerson() != null){
            this.maximumPerson = Integer.parseInt(modifyMeetingReqDTO.getMaximumPerson());
        }
        if(modifyMeetingReqDTO.getMeetingDescription() != null){
            this.meetingDescription = modifyMeetingReqDTO.getMeetingDescription();
        }
        if(modifyMeetingReqDTO.getMeetingDate() != null){
            this.meetingDate = LocalDate.parse(modifyMeetingReqDTO.getMeetingDate());
        }
        if(modifyMeetingReqDTO.getMeetingTime() != null){
            this.meetingTime = LocalTime.parse(modifyMeetingReqDTO.getMeetingTime());
        }
    }

    // 연관관계 편의 메소드
    public void addMeetingRoom(MeetingRoom meetingRoom){
        meetingRooms.add(meetingRoom);
        meetingRoom.setMeeting(this);
    }

    //비지니스 로직
    public void updateStatus(){
        System.out.println(meetingRooms.toString());
        if(meetingRooms.size() == maximumPerson){
            this.meetingStatus = MeetingStatus.N;
        }else{
            this.meetingStatus = MeetingStatus.Y;
        }
    }

    // 모임 방 관계 삭제
    public void removeMeetingRoom(MeetingRoom meetingRoom){
        meetingRooms.remove(meetingRoom);
    }
}
