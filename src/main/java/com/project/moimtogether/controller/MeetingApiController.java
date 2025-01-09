package com.project.moimtogether.controller;

import com.project.moimtogether.config.error.ErrorResponse;
import com.project.moimtogether.config.error.customException.SuccessCode;
import com.project.moimtogether.dto.meeting.MeetingRoomResDTO;
import com.project.moimtogether.dto.meeting.ModifyMeetingReqDTO;
import com.project.moimtogether.dto.meeting.PostMeetingReqDTO;
import com.project.moimtogether.dto.meeting.PostMeetingResDTO;
import com.project.moimtogether.service.meeting.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "[모임 관련 API]")
public class MeetingApiController {

    private final MeetingService meetingService;

    @PostMapping("/meetings")
    @Operation(summary = "모임 방을 생성합니다.")
    public ResponseEntity<PostMeetingResDTO> createMeeting(@Valid @RequestBody PostMeetingReqDTO reqDTO) {
        PostMeetingResDTO postMeetingResDTO = meetingService.createMeetingService(reqDTO);
        return ResponseEntity.ok(postMeetingResDTO);
    }

    @GetMapping("/meetings")
    @Operation(summary = "전체 모임방을 조회합니다.")
    public ResponseEntity<ErrorResponse<List<PostMeetingResDTO>>> searchAllMeetings() {
        List<PostMeetingResDTO> meetingList = meetingService.findAllMeetingList();
        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_SEARCH_SUCCESS,meetingList));
    }

    @GetMapping("/meetings/{meetingId}")
    @Operation(summary = "특정 모임방을 조회합니다.")
    public ResponseEntity<ErrorResponse<MeetingRoomResDTO>> searchMeeting(@PathVariable("meetingId") Long meetingId) {
        MeetingRoomResDTO meetingRoomResDTO = meetingService.findMeetingRoom(meetingId);
        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_SEARCH_SUCCESS,meetingRoomResDTO));
    }

    @PatchMapping("/meetings")
    @Operation(summary = "모임 정보를 수정합니다.")
    public ResponseEntity<ErrorResponse<PostMeetingResDTO>> modifyMeeting(
            @Valid @RequestBody ModifyMeetingReqDTO modifyMeetingReqDTO) {

        PostMeetingResDTO resDTO = meetingService.modifyMeetingService(modifyMeetingReqDTO);
        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_MODIFY_SUCCESS,resDTO));
    }

    @PostMapping("/meetings/join")
    @Operation(summary = "모임에 가입합니다.")
    public ResponseEntity<ErrorResponse<String>> joinMeeting(@RequestBody Map<String, Long> meetingId) {
        Long id = meetingId.get("meetingId");
        meetingService.joinMeetingService(id);
        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_JOIN_SUCCESS,"성공"));
    }

    @DeleteMapping("/meetings/{meetingId}")
    @Operation(summary = "모임을 삭제합니다.")
    public ResponseEntity<ErrorResponse<String>> deleteMeeting(@PathVariable("meetingId") Long meetingId) {
        meetingService.deleteMeetingService(meetingId);
        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_DELETE_SUCCESS,"성공"));
    }

}
