package com.project.moimtogether.domain.notify;

import com.project.moimtogether.domain.meeting.Meeting;
import com.project.moimtogether.domain.reply.Reply;
import com.project.moimtogether.dto.notify.ModifyNotifyDTO;
import com.project.moimtogether.dto.notify.NotifyDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "notify")
public class Notify {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notify_id")
    private Long id;

    @Column(name = "notify_title")
    private String notifyTitle;

    @Column(name = "notify_content")
    private String notifyContent;

    @Column(name = "notify_date")
    private LocalDateTime notifyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @OneToMany(mappedBy = "notify",cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    // 공지사항 생성 메서드
    public static Notify createNotify(NotifyDTO notifyDTO){
        Notify notify = new Notify();
        notify.setNotifyTitle(notifyDTO.getNotifyTitle());
        notify.setNotifyContent(notifyDTO.getNotifyContent());
        notify.setNotifyDate(LocalDateTime.now());
        notify.setMeeting(notifyDTO.getMeeting());

        return notify;
    }

    // 모임 수정 메서드
    public void modifyNotify(ModifyNotifyDTO modifyNotifyDTO) {
        if(modifyNotifyDTO.getNotifyTitle() != null){
            this.notifyTitle = modifyNotifyDTO.getNotifyTitle();
        }
        if(modifyNotifyDTO.getNotifyContent() != null){
            this.notifyContent = modifyNotifyDTO.getNotifyContent();
        }
    }

}
