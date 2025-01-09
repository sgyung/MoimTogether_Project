package com.project.moimtogether.config.error.customException;

import com.project.moimtogether.domain.member.UserErrorCode;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final UserErrorCode userErrorCode;

    public UserException(UserErrorCode userErrorCode) {
        super(userErrorCode.getMemberErrorMsg());
        this.userErrorCode = userErrorCode;
    }

    public UserException(UserErrorCode userErrorCode, String message) {
        super(message);
        this.userErrorCode = userErrorCode;
    }

}
