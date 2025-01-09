package com.project.moimtogether.repository;

import com.project.moimtogether.domain.meetingroom.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {

}
