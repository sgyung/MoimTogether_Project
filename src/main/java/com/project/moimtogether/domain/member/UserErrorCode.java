package com.project.moimtogether.domain.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode {

    /**
     * ErrorCode List
     */
    // 이메일 관련 오류
    EMAIL_FORMAT_ERROR(HttpStatus.BAD_REQUEST,400,"이메일 형식이 유효하지 않습니다."),

    // 아이디 관련 오류
    ID_STRENGTH_ERROR(HttpStatus.BAD_REQUEST, 400,"아이디는 최소 4자 이상이며, 영문 또는 숫자만 사용 가능합니다."),

    // 비밀번호 관련 오류
    PASSWORD_STRENGTH_ERROR(HttpStatus.BAD_REQUEST, 400,"비밀번호는 최소 8자 이상이며, 영문/숫자/특수문자를 각각 하나 이상 포함해야 합니다."),

    // 이름 관련 오류
    NAME_FORMAT_ERROR(HttpStatus.BAD_REQUEST, 400,"이름 형식이 유효하지 않습니다."),

    // 전화번호 관련 오류
    TELEPHONE_FORMAT_ERROR(HttpStatus.BAD_REQUEST, 400,"전화번호 형식이 유효하지 않습니다. ex)0101231234"),

    // 생년월일 관련 유류
    BIRTH_FORMAT_ERROR(HttpStatus.BAD_REQUEST, 400,"생년월일 형식이 유효하지 않습니다. ex)19900101"),

    // 기타 오류 코드
    DEFAULT_ERROR(HttpStatus.BAD_REQUEST, 400,"기타 오류가 발생하였습니다."),

    // 아이디 중복 오류
    ID_ALREADY_EXISTS(HttpStatus.CONFLICT,400,"중복된 아이디 입니다."),

    // 아이디 존재 여부 오류
    ID_NOT_EXISTS(HttpStatus.CONFLICT,404,"아이디가 존재하지 않습니다."),

    // 비밀번호 일치 여부 오류
    PASSWORD_NOT_MATCH(HttpStatus.CONFLICT,404,"비밀번호가 일치하지 않습니다."),

    // 비밀번호 중복 여부 오류
    PASSWORD_DUPLICATE(HttpStatus.CONFLICT,400,"이전 비밀번호를 새로운 비밀번호로 사용할 수 없습니다."),

    // 인증번호 일치 여부 오류
    VERIFICATION_CODE_NOT_MATCH(HttpStatus.CONFLICT,400,"인증번호가 일치하지 않습니다."),

    // 로그인 여부 오류
    NOT_LOGIN_ERROR(HttpStatus.CONFLICT,400,"로그인이 되어있지 않습니다. 로그인 먼저 진행해주세요."),

    // 알람 존재 여부 오류
    NOT_FOUND_ALARM(HttpStatus.CONFLICT,400,"알람을 찾을 수 없습니다."),

    // 회원 모임 여부 오류
    MEETING_ROOM_NOT_EXISTS_ERROR(HttpStatus.BAD_REQUEST,400,"가입된 모임이 없습니다."),

    // DB 오류
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,500,"데이터베이스 오류입니다.");




    // member 실패 코드의 "코드 상태"를 반환
    private final HttpStatus httpStatus;

    // member 실패 코드의 "코드 값"을 반환
    private final int code;

    // member 실패 코드의 "코드 메시지"를 반환
    private final String memberErrorMsg;

    UserErrorCode(HttpStatus httpStatus, int code, String memberErrorMsg) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.memberErrorMsg = memberErrorMsg;
    }
}
