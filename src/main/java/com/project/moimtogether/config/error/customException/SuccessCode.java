package com.project.moimtogether.config.error.customException;

import lombok.Getter;

@Getter
public enum SuccessCode {
    /**
     * SuccessCode
     */
    REGISTER_INSERT_SUCCESS(true,200,"회원가입이 성공적으로 완료되었습니다."),
    MEETING_INSERT_SUCCESS(true,200,"모임 생성이 성공적으로 완료되었습니다."),
    NOTIFY_INSERT_SUCCESS(true,200,"공지사항 등록이 성공적으로 완료되었습니다."),
    REVIEW_INSERT_SUCCESS(true,200,"리뷰 등록이 성공적으로 완료되었습니다."),
    REPLY_INSERT_SUCCESS(true,200,"댓글 등록이 성공적으로 완료되었습니다."),
    COMPLETE_JOIN_SUCCESS(true, 200, "성공적으로 가입을 완료하였습니다."),
    COMPLETE_MODIFY_SUCCESS(true, 200, "수정이 성공적으로 완료되었습니다."),
    COMPLETE_DELETE_SUCCESS(true, 200, "성공적으로 삭제 및 탈퇴를 완료하였습니다."),
    COMPLETE_LIKE_SUCCESS(true, 200, "성공적으로 좋아요를 완료하였습니다."),
    COMPLETE_CANCEL_LIKE_SUCCESS(true, 200, "성공적으로 좋아요 취소를 완료하였습니다."),
    COMPLETE_LOGIN_SUCCESS(true, 200, "성공적으로 로그인이 완료되었습니다."),
    ALARM_NOT_EXISTS(true, 200, "확인할 알람이 없습니다."),
    COMPLETE_SEARCH_SUCCESS(true, 200, "성공적으로 조회를 완료하였습니다."),
    COMPLETE_GET_API_SUCCESS(true, 200, "성공적으로 외부 API에서 데이터를 받았습니다.");

    // 성공 코드의 "코드 상태"를 반환
    private final boolean success;

    // 성공 코드의 "코드 값"을 반환
    private final int code;

    // 성공 코드의 "코드 메시지"를 반환
    private final String errorMsg;


    SuccessCode(boolean success, int code, String errorMsg) {
        this.success = success;
        this.code = code;
        this.errorMsg = errorMsg;
    }
}
