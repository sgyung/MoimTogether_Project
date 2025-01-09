package com.project.moimtogether.dto.meeting;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "모임 수정 요청 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyMeetingReqDTO {

    @NotNull(message = "모임 아이디는 공백일 수 없습니다.")
    private Long meetingId;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9!@#$%^&*()_+=\\.\\s]{1,30}$", message = "모임 제목은 최대 30자까지 입니다.")
    private String meetingTitle;

    @Pattern(regexp = "^[2-5]$",message = "최대 인원은 2명 ~ 5명까지 이며, 숫자만 사용가능합니다.")
    private String maximumPerson;

    @Pattern(regexp = "^(\\d{4})-(\\d{2})-(\\d{2})$", message = "날짜 형식이 유효하지 않습니다. ex)1990-01-01")
    private String meetingDate;

    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "시간 형식이 유효하지 않습니다. ex)00:00")
    private String meetingTime;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9!@#$%^&*()_+=\\.\\s]{1,150}$", message = "모임 상세설명은 최대 150자까지 입니다.")
    private String meetingDescription;

}
