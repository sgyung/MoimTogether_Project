package com.project.moimtogether.controller;

import com.project.moimtogether.config.error.ErrorResponse;
import com.project.moimtogether.config.error.customException.SuccessCode;
import com.project.moimtogether.dto.reply.PostReplyReqDTO;
import com.project.moimtogether.dto.reply.PostReplyResDTO;
import com.project.moimtogether.service.meeting.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "[댓글 관련 API]")
public class ReplyApiController {

    private final ReplyService replyService;

    @PostMapping("/notifys/replys")
    @Operation(summary = "모임 방에 속한 회원들은 댓글을 작성할 수 있다.")
    public ResponseEntity<ErrorResponse<PostReplyResDTO>> registerReply(@Valid @RequestBody PostReplyReqDTO reqDTO){

        PostReplyResDTO replyDTO = replyService.registerReply(reqDTO);

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.REPLY_INSERT_SUCCESS, replyDTO));
    }
}
