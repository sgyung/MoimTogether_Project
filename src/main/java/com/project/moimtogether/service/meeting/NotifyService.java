package com.project.moimtogether.service.meeting;

import com.project.moimtogether.config.error.customException.MeetingException;
import com.project.moimtogether.config.error.customException.UserException;
import com.project.moimtogether.domain.meeting.Meeting;
import com.project.moimtogether.domain.meeting.MeetingErrorCode;
import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.member.UserErrorCode;
import com.project.moimtogether.domain.notify.Notify;
import com.project.moimtogether.dto.notify.*;
import com.project.moimtogether.repository.MeetingRepository;
import com.project.moimtogether.repository.NotifyRepository;
import com.project.moimtogether.repository.query.QueryRepository;
import com.project.moimtogether.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotifyService {
    private final MeetingRepository meetingRepository;
    private final NotifyRepository notifyRepository;
    private final QueryRepository queryRepository;

    // 공지사항 등록 서비스
    public PostNotifyResDTO registerNotifyService(PostNotifyReqDTO reqDTO) {
        Member currentMember = SecurityUtils.getCurrentMember();
        if(currentMember == null) {
            throw new UserException(UserErrorCode.NOT_LOGIN_ERROR);
        }

        Meeting currentMeeting = meetingRepository.findById(reqDTO.getMeetingId())
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.MEETING_NOT_FOUND_ERROR));

        if(!currentMeeting.getMeetingLeader().equals(currentMember.getMemberId())) {
            throw new MeetingException(MeetingErrorCode.PERMISSION_DENIED);
        }

        NotifyDTO notifyDTO = NotifyDTO.builder()
                .notifyTitle(reqDTO.getNotifyTitle())
                .notifyContent(reqDTO.getNotifyContent())
                .meeting(currentMeeting)
                .build();

        try {
            Notify notify = Notify.createNotify(notifyDTO);
            notifyRepository.save(notify);

            return PostNotifyResDTO.builder()
                    .notifyId(notify.getId())
                    .notifyTitle(notify.getNotifyTitle())
                    .notifyContent(notify.getNotifyContent())
                    .notifyDate(notify.getNotifyDate())
                    .meetingId(notify.getMeeting().getMeetingId())
                    .build();
        }catch (Exception e) {
            throw new MeetingException(MeetingErrorCode.UNEXPECTED_ERROR);
        }
    }

    // 공지사항 수정
    public void modifyNotifyService(ModifyNotifyDTO modifyNotifyDTO){
        Member currentMember = SecurityUtils.getCurrentMember();
        if(currentMember == null) {
            throw new UserException(UserErrorCode.NOT_LOGIN_ERROR);
        }

        Notify currentNotify = notifyRepository.findById(modifyNotifyDTO.getNotifyId())
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.NOTIFY_NOT_FOUND_ERROR));

        Meeting currentMeeting = meetingRepository.findById(currentNotify.getMeeting().getMeetingId())
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.MEETING_NOT_FOUND_ERROR));

        if(!currentMeeting.getMeetingLeader().equals(currentMember.getMemberId())) {
            throw new MeetingException(MeetingErrorCode.PERMISSION_DENIED);
        }

        try {
            currentNotify.modifyNotify(modifyNotifyDTO);
        } catch (Exception e) {
            throw new MeetingException(MeetingErrorCode.UNEXPECTED_ERROR);
        }
    }

    // 특정 공지사항 조회
    public SearchNotifyDTO searchNotify(Long notifyId){
        Member currentMember = SecurityUtils.getCurrentMember();
        if(currentMember == null) {
            throw new UserException(UserErrorCode.NOT_LOGIN_ERROR);
        }

        Notify notify = queryRepository.findNotifyWithAll(notifyId);

        return new SearchNotifyDTO(notify);
    }
}
