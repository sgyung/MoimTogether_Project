package com.project.moimtogether.domain.meeting;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MeetingErrorCode {
//
//    // 모임 제목 관련 오류
//    TITLE_STRENGTH_ERROR(HttpStatus.BAD_REQUEST, 400,"모임 제목은 최대 30자까지 입니다."),
//
//    // 최대 인원 관련 오류
//    MAXIMUM_PERSON_STRENGTH_ERROR(HttpStatus.BAD_REQUEST, 400,"최대 인원은 2명 ~ 5명까지 이며, 숫자만 사용가능합니다."),
//
//    // 모임 날짜 관련 오류
//    DATE_FORMAT_ERROR(HttpStatus.BAD_REQUEST, 400,"날짜 형식이 유효하지 않습니다. ex)1990-01-01"),
//
//    // 모임 시간 관련 오류
//    TIME_FORMAT_ERROR(HttpStatus.BAD_REQUEST, 400,"시간 형식이 유효하지 않습니다. ex)00:00"),
//
//    // 모임 설명 관련 유류
//    DESCRIPTION_STRENGTH_ERROR(HttpStatus.BAD_REQUEST, 400,"모임 상세설명은 최대 150자까지 입니다."),

    // 모임 존재 여부 관련 오류
    MEETING_NOT_EXISTS(HttpStatus.BAD_REQUEST, 400,"등록된 모임이 전혀 없습니다."),

    // 모임 수정 권한 관련 오류
    PERMISSION_DENIED(HttpStatus.BAD_REQUEST, 400,"해당 기능은 방장에게만 권한이 있습니다."),

    // 모임 수정시 최대 인원 오류
    MEETING_MODIFY_MAXIMUM_PERSON_ERROR(HttpStatus.BAD_REQUEST, 400,"현재 모임 인원이 수정할려는 최대 인원보다 많습니다."),

    // 모임 삭제시 인원 오류
    MEETING_REMAIN_PERSON_ERROR(HttpStatus.BAD_REQUEST, 400,"현재 모임에 인원이 남아있어서 모임삭제를 할 수 없습니다."),

    // 모임 가입 불가 오류
    MEETING_JOIN_NOT_ALLOWED_ERROR(HttpStatus.BAD_REQUEST, 400,"현재 모임의 정원이 가득차서 가입이 불가능합니다."),

    // 모임 탈퇴 불가 오류
    MEETING_NOT_WITHDRAW_ERROR(HttpStatus.BAD_REQUEST, 400,"모임 탈퇴는 GUEST만 가능합니다."),

    // 모임 조회 오류
    MEETING_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, 400,"모임 아이디에 해당하는 모임이 없습니다."),

    // 모임 가입 여부 오류
    MEETING_NOT_JOIN_ERROR(HttpStatus.BAD_REQUEST, 400,"현재 회원님은 해당 모임에 가입되어있지 않습니다."),

    // 공지사항 조회 오류
    NOTIFY_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, 400,"공지사항 아이디에 해당하는 공지사항이 없습니다."),

    // DB 오류
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,500,"예기치 못한 오류가 발생하였습니다.");

    // meeting 실패 코드의 "코드 상태"를 반환
    private final HttpStatus httpStatus;

    // meeting 실패 코드의 "코드 값"을 반환
    private final int code;

    // meeting 실패 코드의 "코드 메시지"를 반환
    private final String meetingErrorMessage;

    MeetingErrorCode(HttpStatus httpStatus, int code, String meetingErrorMessage) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.meetingErrorMessage = meetingErrorMessage;
    }

}
