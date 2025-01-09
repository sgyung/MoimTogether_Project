package com.project.moimtogether.domain.alarm;

import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.dto.alarm.AlarmDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "alarm")
public class Alarm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Column(name = "alarm_title")
    private String title;

    @Column(name = "alarm_content")
    private String content;

    @Column(name = "alarm_status")
    private AlarmReadStatus alarmReadStatus;

    @Column(name = "alarm_date")
    private LocalDateTime alarmDate;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    // 생성 메서드
    public static Alarm createAlarm(AlarmDTO alarmDTO) {
        Alarm alarm = new Alarm();
        alarm.setTitle(alarmDTO.getTitle());
        alarm.setContent(alarmDTO.getContent());
        alarm.setAlarmReadStatus(AlarmReadStatus.N);
        alarm.setAlarmDate(LocalDateTime.now());
        alarm.setMember(alarmDTO.getMember());
        return alarm;
    }

    //비지니스 로직
    public void confirmation(){
        this.alarmReadStatus = AlarmReadStatus.Y;
    }
}
