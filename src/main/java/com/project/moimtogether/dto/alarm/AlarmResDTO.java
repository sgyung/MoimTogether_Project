package com.project.moimtogether.dto.alarm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "알람 정보 응답 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmResDTO {

    private Long alarmId;
    private String alarmTitle;
    private String alarmContent;
    private LocalDateTime alarmDate;
}
