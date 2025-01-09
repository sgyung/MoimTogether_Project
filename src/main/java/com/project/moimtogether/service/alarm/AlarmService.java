package com.project.moimtogether.service.alarm;

import com.project.moimtogether.config.error.customException.UserException;
import com.project.moimtogether.domain.alarm.Alarm;
import com.project.moimtogether.domain.member.Member;
import com.project.moimtogether.domain.member.UserErrorCode;
import com.project.moimtogether.dto.alarm.AlarmResDTO;
import com.project.moimtogether.repository.AlarmRepository;
import com.project.moimtogether.repository.query.QueryRepository;
import com.project.moimtogether.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final QueryRepository queryRepository;

    // 알림 조회 서비스
    public List<AlarmResDTO> finaAllAlarmService(){
        Member currentMember = SecurityUtils.getCurrentMember();
        if(currentMember == null) {
            throw new UserException(UserErrorCode.NOT_LOGIN_ERROR);
        }
        try {
            List<AlarmResDTO> alarmResDTOList = queryRepository.findNotReadAlarm(currentMember.getId());

            return alarmResDTOList;
        }catch (Exception e){
            throw new UserException(UserErrorCode.DATABASE_ERROR);
        }
    }

    // 알림 확인 서비스
    public void confirmAlarm(Long alarmId){
        Member currentMember = SecurityUtils.getCurrentMember();
        if(currentMember == null) {
            throw new UserException(UserErrorCode.NOT_LOGIN_ERROR);
        }

        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_ALARM));

        alarm.confirmation();

        alarmRepository.save(alarm);
    }
}
