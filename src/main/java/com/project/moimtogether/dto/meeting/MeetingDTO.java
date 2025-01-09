package com.project.moimtogether.dto.meeting;

import com.project.moimtogether.domain.meeting.MeetingStatus;
import com.project.moimtogether.domain.place.Place;
import com.project.moimtogether.dto.place.PlaceDTO;
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
public class MeetingDTO {

    private String meetingTitle;
    private String meetingDescription;
    private String meetingDate;
    private String meetingTime;
    private String maximumPerson;
    private MeetingStatus meetingStatus;
    private String meetingLeader;
    private Place meetingPlace;
}
