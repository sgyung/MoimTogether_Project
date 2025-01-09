package com.project.moimtogether.dto.notify;

import com.project.moimtogether.domain.meeting.Meeting;
import com.project.moimtogether.domain.notify.Notify;
import com.project.moimtogether.dto.reply.ReplyDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "공지사항 조회 객체")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchNotifyDTO {

    private Long meetingId;
    private Long notifyId;
    private String notifyTitle;
    private String notifyContent;
    private LocalDateTime notifyDate;
    private List<ReplyDTO> replyList;

    public SearchNotifyDTO(Notify notify) {
        this.meetingId = notify.getMeeting().getMeetingId();
        this.notifyId = notify.getId();
        this.notifyTitle = notify.getNotifyTitle();
        this.notifyContent = notify.getNotifyContent();
        this.notifyDate = notify.getNotifyDate();
        this.replyList = notify.getReplies().stream()
                .map(reply -> new ReplyDTO(reply))
                .collect(Collectors.toList());
    }
}
