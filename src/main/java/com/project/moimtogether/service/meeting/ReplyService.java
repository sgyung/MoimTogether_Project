package com.project.moimtogether.service.meeting;

import com.project.moimtogether.config.error.customException.MeetingException;
import com.project.moimtogether.config.error.customException.UserException;
import com.project.moimtogether.domain.meeting.Meeting;
import com.project.moimtogether.domain.meeting.MeetingErrorCode;
import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.member.UserErrorCode;
import com.project.moimtogether.domain.notify.Notify;
import com.project.moimtogether.domain.reply.Reply;
import com.project.moimtogether.dto.meeting.MeetingRoomMemberDTO;
import com.project.moimtogether.dto.reply.PostReplyReqDTO;
import com.project.moimtogether.dto.reply.PostReplyResDTO;
import com.project.moimtogether.dto.reply.RegisterReplyDTO;
import com.project.moimtogether.repository.MeetingRepository;
import com.project.moimtogether.repository.NotifyRepository;
import com.project.moimtogether.repository.UserRepository;
import com.project.moimtogether.repository.ReplyRepository;
import com.project.moimtogether.repository.query.QueryRepository;
import com.project.moimtogether.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final QueryRepository queryRepository;

    // 댓글 등록 서비스
    public PostReplyResDTO registerReply(PostReplyReqDTO reqDTO) {
        Member currentMember = SecurityUtils.getCurrentMember();
        if(currentMember == null) {
            throw new UserException(UserErrorCode.NOT_LOGIN_ERROR);
        }
        Notify notify = queryRepository.findNotifyWithMeeting(reqDTO.getNotifyId());

        if(notify == null) {
            throw new MeetingException(MeetingErrorCode.NOTIFY_NOT_FOUND_ERROR);
        }

        MeetingRoomMemberDTO meetingRoomMemberDTO = queryRepository.findMeetingRoomMember(notify.getMeeting().getMeetingId(), currentMember.getMemberId());

        if(meetingRoomMemberDTO == null) {
            throw new MeetingException(MeetingErrorCode.MEETING_NOT_JOIN_ERROR);
        }

        Reply reply = Reply.createReply(new RegisterReplyDTO(reqDTO.getReplyContent(),notify,meetingRoomMemberDTO.getMember()));

        try{
            replyRepository.save(reply);

            return PostReplyResDTO.builder()
                    .notifyId(notify.getId())
                    .replyId(reply.getReplyId())
                    .replyContent(reply.getReplyContent())
                    .replyDate(reply.getReplyDate())
                    .build();
        }catch (Exception e){
            throw new MeetingException(MeetingErrorCode.UNEXPECTED_ERROR);
        }
    }

}
