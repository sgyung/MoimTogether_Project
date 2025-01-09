package com.project.moimtogether.domain.place;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PlaceErrorCode {
    /**
     * ErrorCode List
     */
    // 장소 존재 여부 오류
    PLACE_NOT_EXISTS_ERROR(HttpStatus.BAD_REQUEST,400,"검색하신 장소가 존재하지 않습니다."),
    // 빈 장소 오류
    PLACE_EMPTY_ERROR(HttpStatus.BAD_REQUEST,400,"등록된 장소가 없습니다. 장소를 먼저 등록해주세요."),
    // 좋아요 오류
    LIKE_ID_ERROR(HttpStatus.BAD_REQUEST,400,"해당 좋아요 아이디가 없습니다."),
    // 리뷰 ID 오류
    REVIEW_ID_ERROR(HttpStatus.BAD_REQUEST,400,"리뷰 아이디가 존재하지 않습니다."),
    // 리뷰 존재 여부 오류
    REVIEW_EXISTS_ERROR(HttpStatus.BAD_REQUEST,400,"회원님은 이미 해당 장소에 리뷰를 작성하였습니다."),
    // 좋아요 존재 여부 오류
    LIKE_EXISTS_ERROR(HttpStatus.BAD_REQUEST,400,"회원님은 이미 해당 장소에 좋아요를 등록하였습니다."),
    // DB 오류
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,500,"예기치 못한 오류가 발생하였습니다.");

    // place 실패 코드의 "코드 상태"를 반환
    private final HttpStatus httpStatus;

    // place 실패 코드의 "코드 값"을 반환
    private final int code;

    // place 실패 코드의 "코드 메시지"를 반환
    private final String placeErrorMsg;

    PlaceErrorCode(HttpStatus httpStatus, int code, String placeErrorMsg) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.placeErrorMsg = placeErrorMsg;
    }
}
