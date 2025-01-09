package com.project.moimtogether.config.error.customException;

import lombok.Getter;

@Getter
public class CustomLogicException extends RuntimeException {

  private final ExceptionCode errorCode;

  public CustomLogicException(ExceptionCode errorCode) {
    super(errorCode.getTokenErrorMsg());
    this.errorCode = errorCode;
  }

  public CustomLogicException(ExceptionCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}
