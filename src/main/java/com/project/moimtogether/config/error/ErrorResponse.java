package com.project.moimtogether.config.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.project.moimtogether.config.error.customException.SuccessCode;
import com.project.moimtogether.domain.meeting.MeetingErrorCode;
import com.project.moimtogether.domain.member.UserErrorCode;
import com.project.moimtogether.domain.place.PlaceErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ErrorResponse<T> {
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;
    private final int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 성공
    public ErrorResponse(SuccessCode successCode,T result) {
        this.isSuccess = successCode.isSuccess();
        this.message = successCode.getErrorMsg();
        this.code = successCode.getCode();
        this.result = result;
    }

    public ErrorResponse(Boolean isSuccess, String message, int code) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.code = code;
    }

    public ErrorResponse(Object errorCode) {
        if (errorCode instanceof UserErrorCode) {
            this.isSuccess = false;
            this.message = ((UserErrorCode) errorCode).getMemberErrorMsg();
            this.code = ((UserErrorCode) errorCode).getCode();
        } else if (errorCode instanceof MeetingErrorCode) {
            this.isSuccess = false;
            this.message = ((MeetingErrorCode) errorCode).getMeetingErrorMessage();
            this.code = ((MeetingErrorCode) errorCode).getCode();
        } else if (errorCode instanceof PlaceErrorCode) {
            this.isSuccess = false;
            this.message = ((PlaceErrorCode) errorCode).getPlaceErrorMsg();
            this.code = ((PlaceErrorCode) errorCode).getCode();
        } else {
            // 기본 처리 (알 수 없는 오류 코드)
            this.isSuccess = false;
            this.message = "알 수 없는 오류가 발생했습니다.";
            this.code = 500;  // 기본 서버 오류 코드
        }
    }
}
