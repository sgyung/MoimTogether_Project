package com.project.moimtogether.dto.meeting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "특정 모임 공지사항 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRoomNotifyDTO {

    private Long notifyId;
    private String notifyTitle;
}
