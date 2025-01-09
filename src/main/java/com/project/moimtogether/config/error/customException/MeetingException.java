package com.project.moimtogether.config.error.customException;

import com.project.moimtogether.domain.meeting.MeetingErrorCode;
import lombok.Getter;

@Getter
public class MeetingException extends RuntimeException {

  private final MeetingErrorCode meetingErrorCode;

  public MeetingException(MeetingErrorCode meetingErrorCode) {
    super(meetingErrorCode.getMeetingErrorMessage());
    this.meetingErrorCode = meetingErrorCode;
  }

  public MeetingException(MeetingErrorCode meetingErrorCode, String message) {
    super(message);
    this.meetingErrorCode = meetingErrorCode;
  }
}
