package com.project.moimtogether.controller;

import com.project.moimtogether.config.error.ErrorResponse;
import com.project.moimtogether.config.error.customException.SuccessCode;
import com.project.moimtogether.dto.notify.ModifyNotifyDTO;
import com.project.moimtogether.dto.notify.PostNotifyReqDTO;
import com.project.moimtogether.dto.notify.PostNotifyResDTO;
import com.project.moimtogether.dto.notify.SearchNotifyDTO;
import com.project.moimtogether.service.meeting.NotifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "[공지사항 관련 API]")
public class NotifyApiController {

    private final NotifyService notifyService;

    @PostMapping("/notifys")
    @Operation(summary = "방장은 모임에 공지사항을 등록합니다.")
    public ResponseEntity<ErrorResponse<PostNotifyResDTO>> registerNotify(@Valid @RequestBody PostNotifyReqDTO notifyReqDTO) {

        PostNotifyResDTO postNotifyResDTO = notifyService.registerNotifyService(notifyReqDTO);

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.NOTIFY_INSERT_SUCCESS, postNotifyResDTO));
    }

    @PatchMapping("/notifys")
    @Operation(summary = "방장은 모임에 공지사항을 수정합니다.")
    public ResponseEntity<ErrorResponse<String>> modifyNotify(@Valid @RequestBody ModifyNotifyDTO modifyNotifyDTO) {

        notifyService.modifyNotifyService(modifyNotifyDTO);

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_MODIFY_SUCCESS,"성공"));
    }

    @GetMapping("/notifys/{notifyId}")
    @Operation(summary = "공지사항을 확인 합니다.")
    public ResponseEntity<ErrorResponse<SearchNotifyDTO>> searchNotify(@Valid @PathVariable("notifyId") Long notifyId) {

        SearchNotifyDTO searchNotifyDTO = notifyService.searchNotify(notifyId);

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_SEARCH_SUCCESS, searchNotifyDTO));
    }
}
