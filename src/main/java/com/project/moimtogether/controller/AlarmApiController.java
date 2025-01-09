package com.project.moimtogether.controller;

import com.project.moimtogether.config.error.ErrorResponse;
import com.project.moimtogether.config.error.customException.SuccessCode;
import com.project.moimtogether.dto.alarm.AlarmResDTO;
import com.project.moimtogether.service.alarm.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "[알람 관련 API]")
public class AlarmApiController {

    private final AlarmService alarmService;

    @GetMapping("/alarms")
    @Operation(summary = "회원이 알람 목록을 확인합니다.")
    public ResponseEntity<ErrorResponse<List<AlarmResDTO>>> searchAlarms() {
        List<AlarmResDTO> alarmResDTOList = alarmService.finaAllAlarmService();

        if(alarmResDTOList.isEmpty()){
            return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.ALARM_NOT_EXISTS,alarmResDTOList));
        }
        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_SEARCH_SUCCESS, alarmResDTOList));
    }

    @PatchMapping("/alarms")
    @Operation(summary = "회원이 특정 알람을 읽습니다.")
    public ResponseEntity<ErrorResponse<String>> readAlarms(@RequestBody Map<String, Long> alarmId) {

        Long id = alarmId.get("alarmId");

        alarmService.confirmAlarm(id);

        return ResponseEntity.ok(new ErrorResponse<>(true, "알람을 성공적으로 읽었습니다.", 200));
    }

}
