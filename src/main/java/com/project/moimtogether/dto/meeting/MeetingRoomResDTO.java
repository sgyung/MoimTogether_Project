package com.project.moimtogether.dto.meeting;

import com.project.moimtogether.domain.meeting.MeetingStatus;
import com.project.moimtogether.domain.place.Place;
import com.project.moimtogether.dto.place.PlaceDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "모임 정보 응답 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingRoomResDTO {

    private Long meetingRoomId;
    private String meetingTitle;
    private String meetingDescription;
    private MeetingStatus meetingStatus;
    private List<MeetingRoomMemberResDTO> meetingRoomMembers;
    private List<MeetingRoomNotifyDTO> meetingRoomNotifies;
    private PlaceDTO place;

}
