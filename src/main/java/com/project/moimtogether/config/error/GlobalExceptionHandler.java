package com.project.moimtogether.config.error;

import com.project.moimtogether.config.error.customException.*;
import com.project.moimtogether.domain.meeting.MeetingErrorCode;
import com.project.moimtogether.domain.member.UserErrorCode;
import com.project.moimtogether.dto.meeting.ModifyMeetingReqDTO;
import com.project.moimtogether.dto.meeting.PostMeetingReqDTO;
import com.project.moimtogether.dto.member.FindLoginIdReqDTO;
import com.project.moimtogether.dto.member.FindPasswordReqDTO;
import com.project.moimtogether.dto.member.PostMemberReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // 첫 번째 FieldError만 추출
        FieldError fieldError = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .orElse(null);

        // 오류가 없다면 기본 서버 오류 처리
        if (fieldError == null) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 오류가 발생했습니다.");
        }

        // 필드 오류 메시지 가져오기
        String errorMessage = fieldError.getDefaultMessage(); // message로 설정한 값 반환

        // 오류 메시지와 코드 반환
        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse<?>> handleSignException(UserException e) {
        int errorCode = e.getUserErrorCode().getCode();

        ErrorResponse<?> response = new ErrorResponse<>(e.getUserErrorCode());

        return ResponseEntity.status(errorCode).body(response);
    }

    @ExceptionHandler(CustomLogicException.class)
    public ResponseEntity<ErrorResponse<?>> handleCustomLogicException(CustomLogicException e) {
        int errorCode = e.getErrorCode().getCode();

        ErrorResponse<?> response = new ErrorResponse<>(e.getErrorCode());

        return ResponseEntity.status(errorCode).body(response);
    }

    @ExceptionHandler(MeetingException.class)
    public ResponseEntity<ErrorResponse<?>> handleMeetingException(MeetingException e) {
        int errorCode = e.getMeetingErrorCode().getCode();

        ErrorResponse<?> response = new ErrorResponse<>(e.getMeetingErrorCode());

        return ResponseEntity.status(errorCode).body(response);
    }

    @ExceptionHandler(PlaceException.class)
    public ResponseEntity<ErrorResponse<?>> handlePlaceException(PlaceException e) {
        int errorCode = e.getPlaceErrorCode().getCode();

        ErrorResponse<?> response = new ErrorResponse<>(e.getPlaceErrorCode());

        return ResponseEntity.status(errorCode).body(response);
    }

    // 기본 에러 응답을 생성하는 메소드
    private ResponseEntity<ErrorResponse<?>> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse<?> response = new ErrorResponse<>(false, message, status.value());
        return ResponseEntity.status(status).body(response);
    }
}
