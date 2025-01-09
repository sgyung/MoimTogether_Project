package com.project.moimtogether.repository;

import com.project.moimtogether.domain.meeting.Meeting;
import com.project.moimtogether.domain.notify.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {

    void deleteByMeeting(Meeting meeting);
}
