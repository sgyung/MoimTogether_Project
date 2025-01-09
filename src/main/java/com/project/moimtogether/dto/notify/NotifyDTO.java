package com.project.moimtogether.dto.notify;

import com.project.moimtogether.domain.meeting.Meeting;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "공지사항 정보 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotifyDTO {

    private String notifyTitle;
    private String notifyContent;
    private Meeting meeting;
}
