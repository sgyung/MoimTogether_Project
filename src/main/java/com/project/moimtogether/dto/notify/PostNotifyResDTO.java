package com.project.moimtogether.dto.notify;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "공지 사항 응답 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostNotifyResDTO {

    private Long meetingId;
    private Long notifyId;
    private String notifyTitle;
    private String notifyContent;
    private LocalDateTime notifyDate;

}
