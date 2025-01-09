package com.project.moimtogether.domain.reply;

import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.notify.Notify;
import com.project.moimtogether.dto.reply.RegisterReplyDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "reply")
public class Reply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;

    @Column(name = "reply_content")
    private String replyContent;

    @Column(name = "reply_date")
    private LocalDateTime replyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notify_id", nullable = false)
    private Notify notify;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 생성 메서드
    public static Reply createReply(RegisterReplyDTO ReplyDTO) {
        Reply reply = new Reply();
        reply.setReplyContent(ReplyDTO.getReplyContent());
        reply.setReplyDate(LocalDateTime.now());
        reply.setNotify(ReplyDTO.getNotify());
        reply.setMember(ReplyDTO.getMember());

        return reply;
    }

    // 연관 관계 편의 메서드
    public void setNotify(Notify notify) {
        this.notify = notify;
        notify.getReplies().add(this);
    }
}
