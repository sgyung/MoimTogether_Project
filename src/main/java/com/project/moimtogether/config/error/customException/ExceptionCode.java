package com.project.moimtogether.config.error.customException;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    /**
     * ErrorCode List
     */
    // 서명이 유효하지 않은 오류
    TOKEN_INVALID(HttpStatus.BAD_REQUEST,400,"서명이 유효하지 않습니다."),
    // 유저가 존재 하지 않는 오류
    USER_NONE(HttpStatus.BAD_REQUEST,400,"회원이 존재하지 않습니다."),
    // 입력 정보 일치 오류
    VALUE_NOT_MATCH(HttpStatus.BAD_REQUEST,400,"회원이 존재하지 않습니다."),
    // 리프레시 토큰 만료 오류
    REFRESH_TOKEN_INVALID(HttpStatus.BAD_REQUEST,400,"리프레시 토큰이 만료되었습니다."),
    // 리프레시 토큰 일치 오류
    REFRESH_TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST,400,"리프레시 토큰이 일치하지 않습니다."),
    // 리프레시 토큰 존재 오류
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST,400,"리프레시 토큰이 존재하지 않습니다.");

    // jwt토큰관련 실패 코드의 "코드 상태"를 반환
    private final HttpStatus httpStatus;

    // jwt토큰관련 실패 코드의 "코드 값"을 반환
    private final int code;

    // jwt토큰관련 실패 코드의 "코드 메시지"를 반환
    private final String tokenErrorMsg;

    ExceptionCode(HttpStatus httpStatus, int code, String tokenErrorMsg) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.tokenErrorMsg = tokenErrorMsg;
    }
}
